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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Modal;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jbpm.workbench.cms.client.components.JbpmWbDragComponent;
import org.jbpm.workbench.cms.client.resources.i18n.JbpmWbCMSConstants;
import org.jbpm.workbench.ht.client.editors.taskslist.TaskListPresenter;
import org.uberfire.client.mvp.HasPresenter;
import org.uberfire.client.mvp.PerspectiveManager;
import org.uberfire.ext.layout.editor.client.api.ModalConfigurationContext;
import org.uberfire.ext.layout.editor.client.api.RenderingContext;

@Dependent
public class TaskListLayoutComponent implements JbpmWbDragComponent {

    private PerspectiveManager perspectiveManager;
    private TaskListPresenter taskListPresenter;
    private TranslationService translationService;

    @Inject
    public TaskListLayoutComponent(TaskListPresenter taskListPresenter, TranslationService translationService, PerspectiveManager perspectiveManager) {
        this.taskListPresenter = taskListPresenter;
        this.translationService = translationService;
        this.perspectiveManager = perspectiveManager;
    }

    @Override
    public Modal getConfigurationModal(ModalConfigurationContext ctx) {
        return new Modal();
    }

    @Override
    public String getDragComponentTitle() {
        return translationService.getTranslation(JbpmWbCMSConstants.TaskListTitle);
    }

    @Override
    public IsWidget getPreviewWidget(RenderingContext ctx) {
        initWidget();
        return taskListPresenter.getView();
    }

    @Override
    public IsWidget getShowWidget(RenderingContext ctx) {
        initWidget();
        return taskListPresenter.getView();
    }

    private void initWidget() {
        ((HasPresenter)taskListPresenter.getView()).init(taskListPresenter);
        taskListPresenter.onStartup(perspectiveManager.getCurrentPerspective().getPlace());
        taskListPresenter.onOpen();
    }
}
