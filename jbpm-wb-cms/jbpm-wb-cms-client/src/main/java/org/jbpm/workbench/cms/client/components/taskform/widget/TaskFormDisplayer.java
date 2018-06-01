package org.jbpm.workbench.cms.client.components.taskform.widget;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.security.shared.api.identity.User;
import org.jbpm.workbench.forms.display.FormRenderingSettings;
import org.jbpm.workbench.forms.display.api.KieWorkbenchFormRenderingSettings;
import org.jbpm.workbench.forms.display.service.KieWorkbenchFormsEntryPoint;
import org.jbpm.workbench.forms.service.shared.FormServiceEntryPoint;
import org.jbpm.workbench.ht.model.TaskSummary;
import org.jbpm.workbench.ht.model.events.TaskCompletedEvent;
import org.jbpm.workbench.ht.model.events.TaskRefreshedEvent;
import org.jbpm.workbench.ht.model.events.TaskSelectionEvent;
import org.jbpm.workbench.ht.service.TaskService;
import org.uberfire.mvp.Command;

@Dependent
public class TaskFormDisplayer implements TaskFormDisplayerView.Presenter,
                                          IsElement {

    private TaskFormDisplayerView view;
    private ManagedInstance<TaskFormAction> instance;

    private Caller<FormServiceEntryPoint> formServices;
    private Caller<TaskService> taskService;
    private Caller<KieWorkbenchFormsEntryPoint> service;

    private Event<TaskRefreshedEvent> taskRefreshed;
    private Event<TaskCompletedEvent> taskCompleted;

    private User identity;

    private KieWorkbenchFormRenderingSettings renderingSettings;

    String serverTemplateId;
    String deploymentId;
    Long taskId;

    @Inject
    public TaskFormDisplayer(TaskFormDisplayerView view, ManagedInstance<TaskFormAction> instance, Caller<FormServiceEntryPoint> formServices, Caller<TaskService> taskService, Caller<KieWorkbenchFormsEntryPoint> service, Event<TaskRefreshedEvent> taskRefreshed, Event<TaskCompletedEvent> taskCompleted, User identity) {
        this.view = view;
        this.instance = instance;
        this.formServices = formServices;
        this.taskService = taskService;
        this.service = service;
        this.taskRefreshed = taskRefreshed;
        this.taskCompleted = taskCompleted;
        this.identity = identity;
    }

    @PostConstruct
    public void init() {
        view.init(this);
        view.hide();
    }

    public void hide() {
        view.hide();
    }

    public void show() {
        view.hide();
        instance.destroyAll();

        formServices.call((RemoteCallback<FormRenderingSettings>) settings -> {
            if (settings instanceof KieWorkbenchFormRenderingSettings) {
                renderForm((KieWorkbenchFormRenderingSettings) settings);
            }
        }).getFormDisplayTask(serverTemplateId, deploymentId, taskId);
    }

    private void renderForm(KieWorkbenchFormRenderingSettings renderingSettings) {
        this.renderingSettings = renderingSettings;

        taskService.call((RemoteCallback<TaskSummary>) task -> {

            List<TaskFormAction> actions = new ArrayList<>();

            switch (task.getTaskStatus()) {
                case TASK_STATUS_READY:
                    actions.add(getTaskFormAction("Claim", true, this::claim));
                    break;
                case TASK_STATUS_RESERVED:
                    if (task.getActualOwner().equals(identity.getIdentifier())) {
                        actions.add(getTaskFormAction("Release", false, this::release));
                        actions.add(getTaskFormAction("Start", true, this::start));
                    }
                    break;
                case TASK_STATUS_IN_PROGRESS:
                    if (task.getActualOwner().equals(identity.getIdentifier())) {
                        actions.add(getTaskFormAction("Save", false, this::save));
                        actions.add(getTaskFormAction("Release", false, this::release));
                        actions.add(getTaskFormAction("Complete", true, this::complete));
                    }
                    break;
            }
            view.show(renderingSettings.getRenderingContext(), actions);
        }).getTask(serverTemplateId, deploymentId, taskId);
    }

    private void complete() {
        if(view.isValid()) {
            service.call(response -> {
                taskCompleted.fire(new TaskCompletedEvent(serverTemplateId, deploymentId, taskId));
                hide();
            }).completeTaskFromContext(renderingSettings.getTimestamp(), renderingSettings.getRenderingContext().getModel(), serverTemplateId, deploymentId, taskId);
        }
    }

    private void save() {
        service.call(response -> {
            taskRefreshed.fire(new TaskRefreshedEvent(serverTemplateId, deploymentId, taskId));
            show();
        }).saveTaskStateFromRenderContext(renderingSettings.getTimestamp(), renderingSettings.getRenderingContext().getModel(), serverTemplateId, deploymentId, taskId);
    }

    private void start() {
        service.call(response -> taskService.call((RemoteCallback<Void>) aVoid -> {
            taskRefreshed.fire(new TaskRefreshedEvent(serverTemplateId, deploymentId, taskId));
            show();
        }).startTask(serverTemplateId, deploymentId, taskId)).clearContext(renderingSettings.getTimestamp());
    }

    private void release() {
        service.call(response -> taskService.call((RemoteCallback<Void>) aVoid -> {
            taskRefreshed.fire(new TaskRefreshedEvent(serverTemplateId, deploymentId, taskId));
            show();
        }).releaseTask(serverTemplateId, deploymentId, taskId)).clearContext(renderingSettings.getTimestamp());
    }

    private void claim() {
        taskService.call(response -> service.call((RemoteCallback<Void>) aVoid -> {
            taskRefreshed.fire(new TaskRefreshedEvent(serverTemplateId,
                                                      deploymentId,
                                                      taskId));
            show();
        }).clearContext(renderingSettings.getTimestamp())).claimTask(serverTemplateId, deploymentId, taskId);
    }

    private TaskFormAction getTaskFormAction(String label, boolean primary, Command command) {
        TaskFormAction action = instance.get();
        action.init(label, primary ? ButtonType.PRIMARY : ButtonType.DEFAULT, command);
        return action;
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }

    @Override
    public void startRender(String serverTemplateId, String domainId, Long taskId) {
        this.serverTemplateId = serverTemplateId;
        this.deploymentId = domainId;
        this.taskId = taskId;
        show();
    }

    public void onTaskSelected(@Observes TaskSelectionEvent event){
        startRender(event.getServerTemplateId(), event.getContainerId(), event.getTaskId());
    }
}
