package org.jbpm.workbench.cms.client.components.taskform;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jbpm.workbench.cms.client.components.JbpmWbDragComponent;
import org.jbpm.workbench.cms.client.components.taskform.widget.TaskFormDisplayer;
import org.jbpm.workbench.cms.client.resources.i18n.JbpmWbCMSConstants;
import org.uberfire.ext.layout.editor.client.api.RenderingContext;

@Dependent
public class TaskFormLayoutComponent implements JbpmWbDragComponent {

    private TranslationService translationService;
    private SyncBeanManager beanManager;

    @Inject
    public TaskFormLayoutComponent(TranslationService translationService, SyncBeanManager beanManager) {
        this.translationService = translationService;
        this.beanManager = beanManager;
    }

    @Override
    public String getDragComponentTitle() {
        return translationService.getTranslation(JbpmWbCMSConstants.TaskFormTitle);
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
        TaskFormDisplayer displayer = beanManager.lookupBean(TaskFormDisplayer.class).newInstance();
        return ElementWrapperWidget.getWidget(displayer.getElement());
    }
}
