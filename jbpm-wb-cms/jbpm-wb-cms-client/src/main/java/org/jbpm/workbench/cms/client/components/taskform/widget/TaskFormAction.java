package org.jbpm.workbench.cms.client.components.taskform.widget;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.uberfire.mvp.Command;

@Dependent
public class TaskFormAction implements TaskFormActionView.Presenter, IsElement {

    private Command callback;

    private TaskFormActionView view;

    @Inject
    public TaskFormAction(TaskFormActionView view) {
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
    }

    public void init(String label, ButtonType buttonType, Command callback) {
        view.setLabel(label);

        if(buttonType != null) {
            view.setButtonType(buttonType);
        }

        this.callback = callback;
    }

    @Override
    public void onClick() {
        if(callback != null) {
            callback.execute();
        }
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }
}
