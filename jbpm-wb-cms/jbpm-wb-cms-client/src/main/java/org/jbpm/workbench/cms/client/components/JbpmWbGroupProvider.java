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
package org.jbpm.workbench.cms.client.components;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.container.SyncBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jbpm.workbench.cms.client.resources.i18n.JbpmWbCMSConstants;
import org.uberfire.ext.layout.editor.client.api.LayoutDragComponentGroup;
import org.uberfire.ext.plugin.client.perspective.editor.api.PerspectiveEditorComponentGroupProvider;

/**
 * {@link PerspectiveEditorComponentGroupProvider} holding all the available {@link JbpmWbDragComponent} instances
 */
@ApplicationScoped
public class JbpmWbGroupProvider implements PerspectiveEditorComponentGroupProvider {

    private SyncBeanManager beanManager;
    private TranslationService translationService;

    @Inject
    public JbpmWbGroupProvider(SyncBeanManager beanManager, TranslationService translationService) {
        this.beanManager = beanManager;
        this.translationService = translationService;
    }

    @Override
    public String getName() {
        return translationService.getTranslation(JbpmWbCMSConstants.JbpmWbCMSGroupName);
    }

    @Override
    public LayoutDragComponentGroup getInstance() {
        LayoutDragComponentGroup group = new LayoutDragComponentGroup(getName());
        Collection<SyncBeanDef<JbpmWbDragComponent>> beanDefs = beanManager.lookupBeans(JbpmWbDragComponent.class);
        for (SyncBeanDef<JbpmWbDragComponent> beanDef : beanDefs) {
            JbpmWbDragComponent dragComponent = beanDef.getInstance();
            group.addLayoutDragComponent(dragComponent.getDragComponentTitle(), dragComponent);
        }
        return group;
    }
}
