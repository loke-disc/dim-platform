package platform.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import platform.entity.Asset;
import platform.entity.Task;
import platform.entity.WorkOrder;
import platform.repository.AssetRepository;
import platform.repository.WorkOrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class WorkOrderService {
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private String workOrderId;
    private String assetId;
    private WorkOrder workOrder;
    private final List<Task> tasks = new ArrayList<>();
    private final Map<String, Boolean> taskCompletionStatus = new HashMap<>();

    public Asset fetchOrCreateAsset(String assetId) {
        Optional<Asset> assetOptional = assetRepository.findByAssetId(assetId);
        if (assetOptional.isPresent()) {
            return assetOptional.get();
        } else {
//            Asset newAsset = Asset.builder().assetId(assetId).build();
            Asset newAsset = new Asset();
            newAsset.setAssetId(assetId);
            //enrich others from asset service
            return assetRepository.save(newAsset);
        }
    }

    public WorkOrder fetchOfCreateWorkOrder(String workOrderId, String assetId, List<Task> tasks) {
//        WorkOrder workOrder = WorkOrder.builder().workOrderId(workOrderId).assetId(assetId).status("New").createdAt(LocalDateTime.now()).build();
        Optional<WorkOrder> workOrderOptional = workOrderRepository.findById(workOrderId);
        if (workOrderOptional.isPresent()) {
            return workOrderOptional.get();
        } else {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setWorkOrderId(workOrderId);
            workOrder.setAssetId(assetId);
            workOrder.setStatus("New");
            workOrder.setCreatedAt(LocalDateTime.now());
            workOrder.setTasks(tasks);
            return workOrderRepository.save(workOrder);
        }
//        WorkOrder workOrder = new WorkOrder();
//        workOrder.setWorkOrderId(workOrderId);
//        workOrder.setAssetId(assetId);
//        workOrder.setStatus("New");
//        workOrder.setCreatedAt(LocalDateTime.now());
//        return workOrderRepository.save(workOrder);
    }

//    public void processWorkOrder(String workOrderId, String assetId, String workflowDefinitionJSON) {
//        this.workOrderId = workOrderId;
//        this.assetId = assetId;
//
//        System.out.println("Workflow started for workOrderId - "+workOrderId);
//        log.info("Workflow started for workOrderId - "+workOrderId);
//
//        Asset asset = fetchOrCreateAsset(assetId);
//        workOrder = createWorkOrder(asset);
//
//        WorkflowDefinition workflowDefinition = loadWorkflowDefinition(workflowDefinitionJSON);
//
//        for (TaskDefinition taskDef : workflowDefinition.getTasks()) {
//            Task task = Task.builder().taskId(taskDef.getTaskId()).description(taskDef.getDescription()).status("New").build();
//            tasks.add(task);
//            //check conditions before processing the tasks
//            if (checkConditions(taskDef.getConditions())) {
//                executeTask(taskDef);
//                //wait for task completion signal
//                Workflow.await(() -> taskCompletionStatus.getOrDefault(taskDef.getTaskId(), false));
//                //update task status
//                updateTaskStatus(task.getTaskId(), true);
//                task.setStatus("Complete");
//                updateWorkOrder(task);
//            }
//        }
//        // Complete work order
//        workOrder.setStatus("Complete");
//        updateWorkOrderInDb(workOrder);
//    }
//
//    public void updateTaskStatus(String taskId, boolean status) {
//        taskCompletionStatus.put(taskId, status);
//    }
//
//    private WorkflowDefinition loadWorkflowDefinition(String workflowDefinition) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.readValue(workflowDefinition, WorkflowDefinition.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to parse workflow definition", e);
//        }
//    }
//
//    private boolean checkConditions(List<String> conditions) {
//        for (String condition: conditions) {
//            if (taskCompletionStatus.getOrDefault(condition, false)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
    public void updateWorkOrder(WorkOrder workOrder) {
        workOrder.setLastModifiedAt(LocalDateTime.now());
        workOrderRepository.save(workOrder);
    }

    public void updateWorkOrderInDb(WorkOrder workOrder) {
        workOrder.setLastModifiedAt(LocalDateTime.now());
        workOrderRepository.save(workOrder);
    }

    public WorkOrder updateTaskStatus(String workOrderId, String taskId, String newStatus) {
        Query query = new Query(Criteria.where("_id").is(workOrderId).and("tasks.taskId").is(taskId));
        WorkOrder workOrder = mongoTemplate.findOne(query, WorkOrder.class, "work_orders");
        if (workOrder == null) {
            System.out.println("No matching work order found for ID: " + workOrderId + " and taskId: " + taskId);
        } else {
            System.out.println("Found work order: " + workOrder);
        }
        Update update = new Update().set("tasks.$.status", newStatus);
        mongoTemplate.updateFirst(query, update, "work_orders");
        return mongoTemplate.findOne(query, WorkOrder.class, "work_orders");
    }
//
//    private void executeTask(TaskDefinition taskDef) {
////        switch (taskDef.getServiceType()) {
////            case "domain":
////                invokeActivity(taskDef.getClassName(), taskDef.getMethod(), taskDef.getTaskId());
////                break;
////            case "platform": //can remove reflection call here
////                invokeActivity(taskDef.getClassName(), taskDef.getMethod(), taskDef.getTaskId());
////                break;
////            default:
////                throw new IllegalArgumentException("unknown service type: " + taskDef.getServiceType());
////        }
//        try {
//            platform.model.ActivityOptions activityOptions = taskDef.getActivityOptions();
//            ActivityOptions temporalActivityOptions = ActivityOptions.newBuilder()
//                    .setTaskQueue(activityOptions.getTaskQueue())
//                    .setStartToCloseTimeout(activityOptions.getStartToCloseTimeoutAsDuration())
//                    .setRetryOptions(
//                            RetryOptions.newBuilder()
//                                    .setMaximumAttempts(activityOptions.getRetryOptions().getMaximumAttempts())
//                                    .build()
//                    ).build();
//            invokeActivity(taskDef.getClassName(), taskDef.getMethod(), taskDef.getTaskId(), temporalActivityOptions);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to execute tasks: " +taskDef.getTaskId(), e);
//        }
//    }
//
//    private void invokeActivity(String className, String methodName, String taskId, ActivityOptions temporalActivityOptions) {
//        try {
//            Class<?> activityClass = Class.forName(className);
////            Object activityInstance = activityClass.getDeclaredConstructor().newInstance();
//            Method method = activityClass.getMethod(methodName, String.class);
//            Object activityStub = Workflow.newActivityStub(activityClass, temporalActivityOptions);
//            method.invoke(activityStub, taskId);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to invoke activity method: " + methodName, e);
//        }
//    }
}
