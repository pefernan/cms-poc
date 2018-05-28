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
import java.util.stream.Stream;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.kie.server.controller.api.service.SpecManagementService;
import org.kie.workbench.common.forms.dynamic.model.config.SelectorData;
import org.kie.workbench.common.forms.dynamic.model.config.SelectorDataProvider;
import org.kie.workbench.common.forms.dynamic.service.shared.FormRenderingContext;

@Dependent
public class ServerTemplateDataProvider implements SelectorDataProvider {

    private SpecManagementService managementService;

    @Inject
    public ServerTemplateDataProvider(SpecManagementService managementService) {
        this.managementService = managementService;
    }

    @Override
    public String getProviderName() {
        return this.getClass().getName();
    }

    @Override
    public SelectorData getSelectorData(FormRenderingContext context) {

        Map<String, String> values = new HashMap<>();

        Stream.of(managementService.listServerTemplateKeys().getServerTemplates())
                .forEach(serverTemplateKey -> values.put(serverTemplateKey.getId(), serverTemplateKey.getName()));

        return new SelectorData(values, null);
    }
}
