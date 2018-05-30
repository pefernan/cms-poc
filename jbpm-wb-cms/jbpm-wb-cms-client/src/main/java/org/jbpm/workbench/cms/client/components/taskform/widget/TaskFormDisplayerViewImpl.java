package org.jbpm.workbench.cms.client.components.taskform.widget;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.forms.dynamic.client.DynamicFormRenderer;
import org.kie.workbench.common.forms.dynamic.service.shared.impl.MapModelRenderingContext;

@Templated
public class TaskFormDisplayerViewImpl implements TaskFormDisplayerView,
                                                  IsElement {

    @Inject
    @DataField
    private Div container;

    @Inject
    @DataField
    private DynamicFormRenderer renderer;

    @Inject
    @DataField
    private Div footer;

    @Inject
    @DataField
    private Button start;

    private Presenter presenter;

    @Override
    public void show(MapModelRenderingContext context, List<TaskFormAction> actions) {
        DOMUtil.removeAllChildren(footer);
        renderer.render(context);
        actions.stream().map(TaskFormAction::getElement).forEach(footer::appendChild);
        container.setHidden(false);
    }


    @Override
    public void hide() {
        container.setHidden(true);
    }

    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isValid() {
        return renderer.isValid();
    }

    @EventHandler("start")
    public void onstart(ClickEvent clickEvent) {
        presenter.startRender();
    }
}
