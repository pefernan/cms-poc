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
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jbpm.workbench.forms.client.display.api.StartProcessFormDisplayProvider;
import org.jbpm.workbench.forms.client.display.views.display.EmbeddedFormDisplayer;
import org.jbpm.workbench.forms.display.api.ProcessDisplayerConfig;
import org.jbpm.workbench.pr.model.ProcessDefinitionKey;

@Dependent
public class StartProcessDisplayer implements StartProcessDisplayerView.Presenter,
                                              IsElement {

    private StartProcessFormDisplayProvider startProcessFormDisplayProvider;
    private EmbeddedFormDisplayer embeddedFormDisplayer;
    private StartProcessDisplayerView view;

    private String serverTemplateId;
    private String domainId;
    private String processId;

    @Inject
    public StartProcessDisplayer(StartProcessFormDisplayProvider startProcessFormDisplayProvider, EmbeddedFormDisplayer embeddedFormDisplayer, StartProcessDisplayerView view) {
        this.startProcessFormDisplayProvider = startProcessFormDisplayProvider;
        this.embeddedFormDisplayer = embeddedFormDisplayer;
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

        embeddedFormDisplayer.setOnCloseCommand(this::show);

        show();
    }

    private void show() {
        startProcessFormDisplayProvider.setup(new ProcessDisplayerConfig(new ProcessDefinitionKey(serverTemplateId, domainId, processId), ""), embeddedFormDisplayer);
        view.show();
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }

    @Override
    public IsWidget getContent() {
        return embeddedFormDisplayer.asWidget();
    }
}
