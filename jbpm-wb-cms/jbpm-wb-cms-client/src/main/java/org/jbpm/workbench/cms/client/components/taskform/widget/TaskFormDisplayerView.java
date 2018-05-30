package org.jbpm.workbench.cms.client.components.taskform.widget;

import java.util.Collection;
import java.util.List;

import org.kie.workbench.common.forms.dynamic.service.shared.impl.MapModelRenderingContext;
import org.uberfire.client.mvp.UberElement;

public interface TaskFormDisplayerView extends UberElement<TaskFormDisplayerView.Presenter> {

    void show(MapModelRenderingContext context, List<TaskFormAction> actions);

    void hide();

    boolean isValid();

    interface Presenter {

        void startRender();

    }
}
