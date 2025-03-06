package platform.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.workflow.Workflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.activities.WorkOrderActivities;
import platform.entity.Asset;
import platform.entity.Task;
import platform.entity.WorkOrder;
import platform.model.TaskDefinition;
import platform.model.WorkflowDefinition;
import platform.repository.AssetRepository;
import platform.repository.WorkOrderRepository;
import platform.service.WorkOrderService;
import platform.service.WorkflowDefinitionParser;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class WorkOrderWorkflowImpl implements WorkOrderWorkflow {
//    private WorkOrderService workOrderService;

//    public WorkOrderWorkflowImpl() {}
//
//    public void setWorkOrderService(WorkOrderService workOrderService) {
//        this.workOrderService = workOrderService;
//    }
//
//    @Override
//    public void processWorkOrder(String workOrderId, String assetId, String workflowDefinitionJSON) {
//        log.info("processWorkOrder started");
//        workOrderService.processWorkOrder(workOrderId, assetId, workflowDefinitionJSON);
//        log.info("processWorkOrder complete");
//    }


//    @Autowired
//    private AssetRepository assetRepository;
//    @Autowired
//    private WorkOrderRepository workOrderRepository;

//    private final WorkOrderActivities workOrderActivities;
//    public WorkOrderWorkflowImpl() {
        ActivityOptions activityOptions = ActivityOptions.newBuilder()
                .setTaskQueue("platform-task-queue")
                .setStartToCloseTimeout(Duration.ofMinutes(5))
                .setRetryOptions(
                        RetryOptions.newBuilder()
                                .setMaximumAttempts(3)
                                .build())
                .build();
    private final WorkOrderActivities workOrderActivities = Workflow.newActivityStub(WorkOrderActivities.class, activityOptions);
//    }
    private final WorkflowDefinitionParser workflowDefinitionParser = new WorkflowDefinitionParser();
    private final Map<String, String> taskStatus = new HashMap<>();

    private String workOrderId;
    private String assetId;
    private WorkOrder workOrder;
//    private final List<Task> tasks = new ArrayList<>();
    private final Map<String, Boolean> taskCompletionStatus = new HashMap<>();

    @Override
    public void processWorkOrder(String workOrderId, String assetId, String workflowDefinitionJSON) {
        this.workOrderId = workOrderId;
        this.assetId = assetId;

        System.out.println("Workflow started for workOrderId - "+workOrderId);
        log.info("Workflow started for workOrderId - "+workOrderId);

        WorkflowDefinition workflowDefinition = workflowDefinitionParser.parseWorkflowDefinition(workflowDefinitionJSON);
        this.workOrderId = workflowDefinition.getWorkflowId();
//        Map<String, String> result = workflowDefinition.getTasks().stream().collect(Collectors.toMap(TaskDefinition::getTaskId, task -> "New"));
        List<Task> tasks = workflowDefinitionParser.extractTasks(workflowDefinitionJSON);

        this.workOrder = workOrderActivities.fetchOrCreateWorkOrder(this.workOrderId, assetId, tasks);
        //created wo



        for (TaskDefinition taskDef : workflowDefinition.getTasks()) {
//            Task task = Task.builder().taskId(taskDef.getTaskId()).description(taskDef.getDescription()).status("New").build();
//            Task task = new Task();
//            task.setTaskId(taskDef.getTaskId());
//            task.setDescription(taskDef.getDescription());
//            task.setStatus("New");
//            tasks.add(task);
            //check conditions before processing the tasks
            if (checkConditions(taskDef.getConditions())) {
                executeTask(taskDef);
                //wait for task completion signal
                Workflow.await(() -> taskCompletionStatus.getOrDefault(taskDef.getTaskId(), false));
                //update task status
                this.workOrder = updateTaskStatus(this.workOrderId, taskDef.getTaskId(), "Complete");
//                task.setStatus("Complete");
//                workOrderActivities.updateWorkOrder(workOrder);
            }
        }
        // Complete work order
        workOrder.setStatus("Complete");
        workOrderActivities.updateWorkOrderInDb(workOrder);
    }

    public WorkOrder updateTaskStatus(String workOrderId, String taskId, String status) {
        System.out.println("Updating workorder :"+workOrderId+" task status: "+status+" of task :"+taskId);
        return workOrderActivities.updateTaskStatus(workOrderId, taskId, status);
    }

//    private Asset fetchOrCreateAsset(String assetId) {
//        Optional<Asset> assetOptional = assetRepository.findByAssetId(assetId);
//        if (assetOptional.isPresent()) {
//            return assetOptional.get();
//        } else {
//            Asset newAsset = Asset.builder().assetId(assetId).build();
//            //enrich others from asset service
//            return assetRepository.save(newAsset);
//        }
//    }
//
//    private WorkOrder createWorkOrder(Asset asset) {
//        WorkOrder workOrder = WorkOrder.builder().workOrderId(workOrderId).status("New").createdAt(LocalDateTime.now()).build();
//        return workOrderRepository.save(workOrder);
//    }

    private WorkflowDefinition loadWorkflowDefinition(String workflowDefinition) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(workflowDefinition, WorkflowDefinition.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse workflow definition", e);
        }
    }

    private boolean checkConditions(List<String> conditions) {
        for (String condition: conditions) {
            if (!taskCompletionStatus.getOrDefault(condition, false)) {
                return false;
            }
        }
        return true;
    }

//    private void updateWorkOrder(WorkOrder workOrder) {
//        workOrder.setLastModifiedAt(LocalDateTime.now());
//        workOrderRepository.save(workOrder);
//    }
//
//    private void updateWorkOrderInDb(WorkOrder workOrder) {
//        workOrder.setLastModifiedAt(LocalDateTime.now());
//        workOrderRepository.save(workOrder);
//    }

    private void executeTask(TaskDefinition taskDef) {
//        switch (taskDef.getServiceType()) {
//            case "domain":
//                invokeActivity(taskDef.getClassName(), taskDef.getMethod(), taskDef.getTaskId());
//                break;
//            case "platform": //can remove reflection call here
//                invokeActivity(taskDef.getClassName(), taskDef.getMethod(), taskDef.getTaskId());
//                break;
//            default:
//                throw new IllegalArgumentException("unknown service type: " + taskDef.getServiceType());
//        }
        try {
            platform.model.ActivityOptions activityOptions = taskDef.getActivityOptions();
            ActivityOptions temporalActivityOptions = ActivityOptions.newBuilder()
                    .setTaskQueue(activityOptions.getTaskQueue())
                    .setStartToCloseTimeout(activityOptions.getStartToCloseTimeoutAsDuration())
                    .setRetryOptions(
                            RetryOptions.newBuilder()
                                    .setMaximumAttempts(activityOptions.getRetryOptions().getMaximumAttempts())
                                    .build()
                    ).build();
            invokeActivity(taskDef.getClassName(), taskDef.getMethod(), taskDef.getTaskId(), taskDef.getServiceType(), temporalActivityOptions);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute tasks: " +taskDef.getTaskId(), e);
        }
    }

    private void invokeActivity(String className, String methodName, String taskId, String serviceType, ActivityOptions temporalActivityOptions) {
        try {
            Class<?> activityClass = Class.forName(className);
//            Object activityInstance = activityClass.getDeclaredConstructor().newInstance();
            Method method;
            Object activityStub;
            boolean status = false;
            switch (serviceType) {
                case "domain":
                    method = activityClass.getMethod("executeTask", String.class, String.class);
                    activityStub = Workflow.newActivityStub(activityClass, temporalActivityOptions);
                    status = (boolean) method.invoke(activityStub, methodName, taskId);
                    break;
                case "platform":
                    method = activityClass.getMethod(methodName, String.class);
                    activityStub = Workflow.newActivityStub(activityClass, temporalActivityOptions);
                    status = (boolean) method.invoke(activityStub, taskId);
                    break;
            }
//            method = activityClass.getMethod(methodName, String.class);
//            Object activityStub = Workflow.newActivityStub(activityClass, temporalActivityOptions);
//            boolean status = (boolean) method.invoke(activityStub, taskId);
            taskCompletionStatus.put(taskId, status);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke activity method: " + methodName, e);
        }
    }
}
