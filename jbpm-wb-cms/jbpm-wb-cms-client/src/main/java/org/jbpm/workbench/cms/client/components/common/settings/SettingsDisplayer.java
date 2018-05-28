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

package org.jbpm.workbench.cms.client.components.common.settings;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.kie.soup.commons.validation.PortablePreconditions;
import org.uberfire.mvp.Command;

@Dependent
public class SettingsDisplayer implements SettingsDisplayerView.Presenter {

    private SettingsDisplayerView view;

    private Object settings;

    private Command acceptCommand;

    private Command cancelCommand;

    @Inject
    public SettingsDisplayer(SettingsDisplayerView view) {
        this.view = view;
    }

    @PostConstruct
    public void setup() {
        view.init(this);
    }

    public void init(Object settings, Command acceptCommand, Command cancelCommand) {
        PortablePreconditions.checkNotNull("settings", settings);
        PortablePreconditions.checkNotNull("acceptCommand", acceptCommand);
        PortablePreconditions.checkNotNull("cancelCommand", cancelCommand);

        this.settings = settings;
        this.acceptCommand = acceptCommand;
        this.cancelCommand = cancelCommand;
    }

    public SettingsDisplayerView getView() {
        return view;
    }

    @Override
    public Object getSettings() {
        return settings;
    }

    @Override
    public void onAccept() {
        acceptCommand.execute();
    }

    @Override
    public void onCancel() {
        cancelCommand.execute();
    }
}
