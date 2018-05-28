/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.forms.cms.components.client.ui;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.container.SyncBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.forms.cms.components.client.resources.i18n.CMSComponentsConstants;
import org.uberfire.ext.layout.editor.client.api.LayoutDragComponentGroup;
import org.uberfire.ext.plugin.client.perspective.editor.api.PerspectiveEditorComponentGroupProvider;

/**
 * {@link PerspectiveEditorComponentGroupProvider} holding all the available {@link DataManagementDragComponent} instances
 */
@ApplicationScoped
public class DataManagementGroupProvider implements PerspectiveEditorComponentGroupProvider {

    private SyncBeanManager beanManager;
    private TranslationService translationService;

    @Inject
    public DataManagementGroupProvider(SyncBeanManager beanManager, TranslationService translationService) {
        this.beanManager = beanManager;
        this.translationService = translationService;
    }

    @Override
    public String getName() {
        return translationService.getTranslation(CMSComponentsConstants.DataManagementGroupName);
    }

    @Override
    public LayoutDragComponentGroup getInstance() {
        LayoutDragComponentGroup group = new LayoutDragComponentGroup(getName());
        Collection<SyncBeanDef<DataManagementDragComponent>> beanDefs = beanManager.lookupBeans(DataManagementDragComponent.class);
        for (SyncBeanDef<DataManagementDragComponent> beanDef : beanDefs) {
            DataManagementDragComponent dragComponent = beanDef.getInstance();
            group.addLayoutDragComponent(dragComponent.getDragComponentTitle(), dragComponent);
        }
        return group;
    }
}
