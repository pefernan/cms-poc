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

package org.jbpm.workbench.cms.client.components.startProcessForm;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Modal;
import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jbpm.workbench.cms.client.components.JbpmWbDragComponent;
import org.jbpm.workbench.cms.client.components.common.settings.SettingsDisplayer;
import org.jbpm.workbench.cms.client.components.startProcessForm.widget.StartProcessDisplayer;
import org.jbpm.workbench.cms.client.resources.i18n.JbpmWbCMSConstants;
import org.jbpm.workbench.cms.shared.model.settings.StartProcessFormSettings;
import org.uberfire.ext.layout.editor.client.api.HasModalConfiguration;
import org.uberfire.ext.layout.editor.client.api.ModalConfigurationContext;
import org.uberfire.ext.layout.editor.client.api.RenderingContext;

@Dependent
public class StartProcessFormLayoutComponent implements JbpmWbDragComponent,
                                                        HasModalConfiguration {

    private final static String ServerTemplateId = "serverTemplateId";
    private final static String DomainId = "domainId";
    private final static String ProcessId = "processId";

    private StartProcessFormSettings settings;
    private SettingsDisplayer settingsDisplayer;
    private TranslationService translationService;
    private StartProcessDisplayer displayer;

    @Inject
    public StartProcessFormLayoutComponent(SettingsDisplayer settingsDisplayer, TranslationService translationService, StartProcessDisplayer displayer) {
        this.settingsDisplayer = settingsDisplayer;
        this.translationService = translationService;
        this.displayer = displayer;
    }

    @Override
    public String getDragComponentTitle() {
        return translationService.getTranslation(JbpmWbCMSConstants.StartProcessFormTitle);
    }

    @Override
    public IsWidget getPreviewWidget(RenderingContext ctx) {

        settings = fromMap(ctx.getComponent().getProperties());

        return getWidget();
    }

    @Override
    public IsWidget getShowWidget(RenderingContext ctx) {
        settings = fromMap(ctx.getComponent().getProperties());

        return getWidget();
    }

    @Override
    public Modal getConfigurationModal(ModalConfigurationContext ctx) {
        settings = fromMap(ctx.getComponentProperties());

        settingsDisplayer.init(settings,
                               () -> {
                                   Map<String, String> settingsMap = toMap(settings);
                                   settingsMap.forEach((key, value) -> ctx.setComponentProperty(key, value));
                                   getWidget();
                                   ctx.configurationFinished();
                               },
                               () -> {
                                   settings = fromMap(ctx.getComponentProperties());
                                   ctx.configurationCancelled();
                               });
        return settingsDisplayer.getView().getPropertiesModal();
    }

    private IsWidget getWidget() {
        displayer.show(settings.getServerTemplateId(), settings.getDomainId(), settings.getProcessId());
        return ElementWrapperWidget.getWidget(displayer.getElement());
    }

    private Map<String, String> toMap(StartProcessFormSettings settings) {
        Map<String, String> result = new HashMap<>();
        result.put(ServerTemplateId, settings.getServerTemplateId());
        result.put(DomainId, settings.getDomainId());
        result.put(ProcessId, settings.getProcessId());
        return result;
    }

    private StartProcessFormSettings fromMap(Map<String, String> componentProperties) {
        return new StartProcessFormSettings(componentProperties.get(ServerTemplateId),
                                            componentProperties.get(DomainId),
                                            componentProperties.get(ProcessId));
    }
}
