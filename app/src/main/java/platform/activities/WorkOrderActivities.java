package platform.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import platform.entity.Task;
import platform.entity.WorkOrder;

import java.util.List;

@ActivityInterface
public interface WorkOrderActivities {
    @ActivityMethod
    WorkOrder fetchOrCreateWorkOrder(String workOrderId, String assetId, List<Task> tasks);

    @ActivityMethod
    WorkOrder updateTaskStatus(String workOrderId, String taskId, String newStatus);

    @ActivityMethod
    void updateWorkOrder(WorkOrder workOrder);

    @ActivityMethod
    void updateWorkOrderInDb(WorkOrder workOrder);
}
