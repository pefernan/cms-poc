package org.jbpm.workbench.cms.client.components.taskform.widget;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated
public class TaskFormActionViewImpl implements TaskFormActionView,
                                               IsElement {

    private Presenter presenter;

    @Inject
    @DataField
    private Button button;

    @Override
    public void init(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLabel(String label) {
        button.setTextContent(label);
    }

    @Override
    public void setButtonType(ButtonType type) {
        DOMUtil.addEnumStyleName(button, type);
    }

    @EventHandler("button")
    public void onClick(ClickEvent event) {
        presenter.onClick();
    }
}
