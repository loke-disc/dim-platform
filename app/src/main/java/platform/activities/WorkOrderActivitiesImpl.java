package platform.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.entity.Task;
import platform.entity.WorkOrder;
import platform.service.WorkOrderService;

import java.util.List;

@Service
public class WorkOrderActivitiesImpl implements WorkOrderActivities {

    @Autowired
    private WorkOrderService workOrderService;

    @Override
    public WorkOrder fetchOrCreateWorkOrder(String workOrderId, String assetId, List<Task> tasks) {
        workOrderService.fetchOrCreateAsset(assetId);
        WorkOrder workOrder = workOrderService.fetchOfCreateWorkOrder(workOrderId, assetId, tasks);
        return workOrder;
    }

    @Override
    public WorkOrder updateTaskStatus(String workOrderId, String taskId, String newStatus) {
        return workOrderService.updateTaskStatus(workOrderId, taskId, newStatus);
    }

    @Override
    public void updateWorkOrder(WorkOrder workOrder) {
        workOrderService.updateWorkOrder(workOrder);
    }

    @Override
    public void updateWorkOrderInDb(WorkOrder workOrder) {
        workOrderService.updateWorkOrderInDb(workOrder);
    }
}
