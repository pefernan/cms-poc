package org.jbpm.workbench.cms.shared.model.settings;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
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
public class TaskInboxFormSettings {

    @FormField(
            type = ListBoxFieldType.class,
            required = true,
            labelKey = "serverTemplateId"
    )
    @SelectorDataProvider(type = SelectorDataProvider.ProviderType.REMOTE, className = "org.jbpm.workbench.cms.service.backend.ServerTemplateDataProvider")
    private String serverTemplateId;

    public TaskInboxFormSettings() {
    }

    public TaskInboxFormSettings(@MapsTo("serverTemplateId") String serverTemplateId) {
        this.serverTemplateId = serverTemplateId;
    }

    public String getServerTemplateId() {
        return serverTemplateId;
    }

    public void setServerTemplateId(String serverTemplateId) {
        this.serverTemplateId = serverTemplateId;
    }
}
