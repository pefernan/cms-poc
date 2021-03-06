/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.forms.cms.components.client.ui.displayer;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.kie.soup.commons.validation.PortablePreconditions;
import org.kie.workbench.common.forms.dynamic.client.DynamicFormRenderer;
import org.kie.workbench.common.forms.dynamic.service.shared.FormRenderingContext;
import org.uberfire.mvp.Command;

@Dependent
public class FormDisplayer implements IsElement,
                                      FormDisplayerView.Presenter {

    private FormDisplayerView view;

    private DynamicFormRenderer formRenderer;

    private Command onSubmit;

    private Command onCancel;

    @Inject
    public FormDisplayer(FormDisplayerView view,
                         DynamicFormRenderer formRenderer) {
        this.view = view;
        this.formRenderer = formRenderer;

        view.init(this);
    }

    public void init(FormRenderingContext context,
                     Command onSubmit,
                     Command onCancel) {
        PortablePreconditions.checkNotNull("context",
                                           context);
        PortablePreconditions.checkNotNull("onSubmit",
                                           onSubmit);
        PortablePreconditions.checkNotNull("onCancel",
                                           onCancel);

        this.formRenderer.render(context);

        this.onSubmit = onSubmit;
        this.onCancel = onCancel;
    }

    public void setEnabled(boolean enabled) {
        view.getElement().getStyle().setProperty("visible", enabled ? "block" : "none");
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }

    @Override
    public DynamicFormRenderer getRenderer() {
        return formRenderer;
    }

    @Override
    public void onSubmit() {
        if (formRenderer.isValid()) {
            if (onSubmit != null) {
                onSubmit.execute();
            }
        }
    }

    @Override
    public void onCancel() {
        if (onCancel != null) {
            onCancel.execute();
        }
    }
}
