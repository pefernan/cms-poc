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

package org.jbpm.workbench.cms.client.components.taskList;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Modal;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jbpm.workbench.cms.client.components.JbpmWbDragComponent;
import org.jbpm.workbench.cms.client.components.common.settings.SettingsDisplayer;
import org.jbpm.workbench.cms.client.resources.i18n.JbpmWbCMSConstants;
import org.jbpm.workbench.cms.shared.model.settings.TaskInboxFormSettings;
import org.jbpm.workbench.ht.client.editors.taskslist.TaskListPresenter;
import org.uberfire.client.mvp.HasPresenter;
import org.uberfire.client.mvp.PerspectiveManager;
import org.uberfire.ext.layout.editor.client.api.HasModalConfiguration;
import org.uberfire.ext.layout.editor.client.api.ModalConfigurationContext;
import org.uberfire.ext.layout.editor.client.api.RenderingContext;

@Dependent
public class TaskInboxLayoutComponent implements JbpmWbDragComponent,
                                                 HasModalConfiguration {

    private final static String ServerTemplateId = "serverTemplateId";

    private PerspectiveManager perspectiveManager;
    private TranslationService translationService;
    private TaskInboxFormSettings settings;
    private SettingsDisplayer settingsDisplayer;
    private SyncBeanManager beanManager;

    @Inject
    public TaskInboxLayoutComponent(TranslationService translationService,
                                    PerspectiveManager perspectiveManager,
                                    SettingsDisplayer settingsDisplayer,
                                    SyncBeanManager beanManager) {
        this.translationService = translationService;
        this.perspectiveManager = perspectiveManager;
        this.settingsDisplayer = settingsDisplayer;
        this.beanManager = beanManager;
    }

    @Override
    public Modal getConfigurationModal(ModalConfigurationContext ctx) {
        settings = fromMap(ctx.getComponentProperties());

        settingsDisplayer.init(settings,
                               () -> {
                                   Map<String, String> settingsMap = toMap(settings);
                                   settingsMap.forEach((key, value) -> ctx.setComponentProperty(key,
                                                                                                value));
                                   ctx.configurationFinished();
                               },
                               () -> {
                                   settings = fromMap(ctx.getComponentProperties());
                                   ctx.configurationCancelled();
                               });
        return settingsDisplayer.getView().getPropertiesModal();
    }

    @Override
    public String getDragComponentTitle() {
        return translationService.getTranslation(JbpmWbCMSConstants.TaskInboxTitle);
    }

    @Override
    public IsWidget getPreviewWidget(RenderingContext ctx) {
        return getWidget(ctx);
    }

    @Override
    public IsWidget getShowWidget(RenderingContext ctx) {
        return getWidget(ctx);
    }

    private IsWidget getWidget(RenderingContext ctx) {
        settings = fromMap(ctx.getComponent().getProperties());
        TaskListPresenter taskListPresenter = beanManager.lookupBean(TaskListPresenter.class).newInstance();
        ((HasPresenter) taskListPresenter.getView()).init(taskListPresenter);
        taskListPresenter.onStartup(perspectiveManager.getCurrentPerspective().getPlace());
        taskListPresenter.setSelectedServerTemplate(settings.getServerTemplateId());
        taskListPresenter.onOpen();
        taskListPresenter.hideActiveFilters();
        return taskListPresenter.getView();
    }

    private Map<String, String> toMap(TaskInboxFormSettings settings) {
        Map<String, String> result = new HashMap<>();
        result.put(ServerTemplateId,
                   settings.getServerTemplateId());
        return result;
    }

    private TaskInboxFormSettings fromMap(Map<String, String> componentProperties) {
        return new TaskInboxFormSettings(componentProperties.get(ServerTemplateId));
    }
}
