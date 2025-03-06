package platform.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.workflow.WorkOrderWorkflow;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowStarter {

    @Autowired
    private WorkflowClient workflowClient;

    public void startWorkflow(String workOrderId, String assetId, String workflowDefinition) {
        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder().setTaskQueue("platform-task-queue").setWorkflowId(workOrderId).build();
        WorkOrderWorkflow workflow = workflowClient.newWorkflowStub(WorkOrderWorkflow.class, workflowOptions);
        WorkflowClient.start(workflow::processWorkOrder, workOrderId, assetId, workflowDefinition);
    }
}
