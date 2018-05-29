/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.workbench.cms.client.components.startProcessForm.widget;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jbpm.workbench.forms.client.i18n.Constants;
import org.jbpm.workbench.forms.display.FormRenderingSettings;
import org.jbpm.workbench.forms.display.api.KieWorkbenchFormRenderingSettings;
import org.jbpm.workbench.forms.display.service.KieWorkbenchFormsEntryPoint;
import org.jbpm.workbench.forms.service.shared.FormServiceEntryPoint;
import org.jbpm.workbench.pr.events.NewProcessInstanceEvent;
import org.uberfire.workbench.events.NotificationEvent;

@Dependent
public class StartProcessDisplayer implements StartProcessDisplayerView.Presenter,
                                              IsElement {

    private Caller<FormServiceEntryPoint> formServices;
    private Caller<KieWorkbenchFormsEntryPoint> service;
    private Event<NewProcessInstanceEvent> newProcessInstanceEvent;
    private Event<NotificationEvent> notificationEvent;

    private StartProcessDisplayerView view;

    private String serverTemplateId;
    private String domainId;
    private String processId;

    private KieWorkbenchFormRenderingSettings renderingSettings;

    @Inject
    public StartProcessDisplayer(Caller<FormServiceEntryPoint> formServices,
                                 Caller<KieWorkbenchFormsEntryPoint> service,
                                 Event<NewProcessInstanceEvent> newProcessInstanceEvent,
                                 Event<NotificationEvent> notificationEvent,
                                 StartProcessDisplayerView view) {
        this.formServices = formServices;
        this.service = service;
        this.newProcessInstanceEvent = newProcessInstanceEvent;
        this.notificationEvent = notificationEvent;
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
    }

    public void show(final String serverTemplateId, final String domainId, final String processId) {
        this.serverTemplateId = serverTemplateId;
        this.domainId = domainId;
        this.processId = processId;

        loadForm();
    }

    private void loadForm() {
        formServices.call((RemoteCallback<FormRenderingSettings>) settings -> {
            if (settings == null) {
                view.showFormNotFoundError(processId);
            } else {
                if (settings instanceof KieWorkbenchFormRenderingSettings) {
                    renderingSettings = (KieWorkbenchFormRenderingSettings) settings;
                    view.show(renderingSettings.getRenderingContext());
                }
            }
        }).getFormDisplayProcess(serverTemplateId, domainId, processId);
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }

    @Override
    public void submit() {
        service.call(new RemoteCallback<Long>() {
            @Override
            public void callback(Long processInstanceId) {
                loadForm();
                newProcessInstanceEvent.fire(new NewProcessInstanceEvent(serverTemplateId,
                                                                         domainId,
                                                                         processInstanceId,
                                                                         processId,
                                                                         processId,
                                                                         1));
                final String message = Constants.INSTANCE.ProcessStarted(processInstanceId);
                notificationEvent.fire(new NotificationEvent(message, NotificationEvent.NotificationType.SUCCESS));
            }
        }).startProcessFromRenderContext(
                renderingSettings.getTimestamp(),
                renderingSettings.getRenderingContext().getModel(),
                serverTemplateId,
                domainId,
                processId,
                null);
    }
}
