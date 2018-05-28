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

package org.jbpm.workbench.cms.service.backend;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jbpm.workbench.cms.shared.model.settings.StartProcessFormSettings;
import org.jbpm.workbench.ks.integration.KieServerIntegration;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.workbench.common.forms.dynamic.model.config.SelectorData;
import org.kie.workbench.common.forms.dynamic.model.config.SelectorDataProvider;
import org.kie.workbench.common.forms.dynamic.service.shared.FormRenderingContext;

@Dependent
public class ProcessIdDataProvider implements SelectorDataProvider {

    private KieServerIntegration kieServerIntegration;

    @Inject
    public ProcessIdDataProvider(KieServerIntegration kieServerIntegration) {
        this.kieServerIntegration = kieServerIntegration;
    }

    @Override
    public String getProviderName() {
        return getClass().getName();
    }

    @Override
    public SelectorData getSelectorData(FormRenderingContext context) {

        StartProcessFormSettings settings = (StartProcessFormSettings) context.getModel();

        Map<String, String> values = new HashMap<>();
        if(!StringUtils.isEmpty(settings.getServerTemplateId()) && !StringUtils.isEmpty(settings.getDomainId())) {

            KieServicesClient client = kieServerIntegration.getServerClient(settings.getServerTemplateId(), settings.getDomainId());

            QueryServicesClient queryClient = client.getServicesClient(QueryServicesClient.class);

            queryClient.findProcesses(0, Integer.MAX_VALUE).stream().forEach(processDefinition -> values.put(processDefinition.getId(), processDefinition.getName()));
        }

        return new SelectorData(values, null);
    }
}
