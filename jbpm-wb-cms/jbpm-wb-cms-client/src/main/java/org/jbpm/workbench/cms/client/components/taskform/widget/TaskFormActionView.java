package org.jbpm.workbench.cms.client.components.taskform.widget;

import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.uberfire.client.mvp.UberElement;

public interface TaskFormActionView extends UberElement<TaskFormActionView.Presenter> {

    void setLabel(String label);

    void setButtonType(ButtonType type);

    interface Presenter {

        void onClick();
    }
}
