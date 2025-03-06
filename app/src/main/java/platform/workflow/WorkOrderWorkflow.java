package platform.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WorkOrderWorkflow {
    @WorkflowMethod
    void processWorkOrder(String workOrderId, String assetId, String workflowDefinition);
}
