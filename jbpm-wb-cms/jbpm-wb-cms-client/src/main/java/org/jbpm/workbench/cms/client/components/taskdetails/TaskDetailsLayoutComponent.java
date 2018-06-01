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

package org.jbpm.workbench.cms.client.components.taskdetails;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jbpm.workbench.cms.client.components.JbpmWbDragComponent;
import org.jbpm.workbench.cms.client.resources.i18n.JbpmWbCMSConstants;
import org.jbpm.workbench.ht.client.editors.taskdetails.TaskDetailsPresenter;
import org.uberfire.client.mvp.PerspectiveManager;
import org.uberfire.ext.layout.editor.client.api.RenderingContext;

@Dependent
public class TaskDetailsLayoutComponent implements JbpmWbDragComponent {

    private PerspectiveManager perspectiveManager;
    private TranslationService translationService;
    private SyncBeanManager beanManager;

    @Inject
    public TaskDetailsLayoutComponent(TranslationService translationService,
                                      PerspectiveManager perspectiveManager,
                                      SyncBeanManager beanManager) {
        this.translationService = translationService;
        this.perspectiveManager = perspectiveManager;
        this.beanManager = beanManager;
    }

    @Override
    public String getDragComponentTitle() {
        return translationService.getTranslation(JbpmWbCMSConstants.TaskDetailsTitle);
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
        TaskDetailsPresenter presenter = beanManager.lookupBean(TaskDetailsPresenter.class).newInstance();
        return presenter.getView();
    }
}
