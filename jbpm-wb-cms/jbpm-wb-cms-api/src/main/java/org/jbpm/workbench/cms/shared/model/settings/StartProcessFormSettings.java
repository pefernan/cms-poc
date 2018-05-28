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

package org.jbpm.workbench.cms.shared.model.settings;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FieldParam;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.forms.adf.definitions.annotations.field.selector.SelectorDataProvider;
import org.kie.workbench.common.forms.adf.definitions.annotations.i18n.I18nSettings;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.selectors.listBox.type.ListBoxFieldType;

@Portable
@Bindable
@FormDefinition(
        startElement = "serverTemplateId",
        i18n = @I18nSettings(keyPreffix = "JbpmWbCMSSettings")
)
public class StartProcessFormSettings {

    @FormField(
            type = ListBoxFieldType.class,
            required = true,
            labelKey = "serverTemplateId"
    )
    @SelectorDataProvider(type = SelectorDataProvider.ProviderType.REMOTE, className = "org.jbpm.workbench.cms.service.backend.ServerTemplateDataProvider")
    private String serverTemplateId;

    @FormField(
            type = ListBoxFieldType.class,
            afterElement = "serverTemplateId",
            required = true,
            labelKey = "domainId",
            settings = {@FieldParam(name = "relatedField", value = "serverTemplateId")}
    )
    @SelectorDataProvider(type = SelectorDataProvider.ProviderType.REMOTE, className = "org.jbpm.workbench.cms.service.backend.DomainIdDataProvider")
    private String domainId;

    @FormField(
            type = ListBoxFieldType.class,
            afterElement = "domainId",
            required = true,
            labelKey = "processId",
            settings = {@FieldParam(name = "relatedField", value = "domainId")}
    )
    @SelectorDataProvider(type = SelectorDataProvider.ProviderType.REMOTE, className = "org.jbpm.workbench.cms.service.backend.ProcessIdDataProvider")
    private String processId;

    public StartProcessFormSettings() {
    }

    public StartProcessFormSettings(@MapsTo("serverTemplateId") String serverTemplateId, @MapsTo("domainId") String domainId, @MapsTo("processId") String processId) {
        this.serverTemplateId = serverTemplateId;
        this.domainId = domainId;
        this.processId = processId;
    }

    public String getServerTemplateId() {
        return serverTemplateId;
    }

    public void setServerTemplateId(String serverTemplateId) {
        this.serverTemplateId = serverTemplateId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
