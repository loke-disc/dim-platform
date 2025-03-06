package platform;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class StartWorkOrder {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        //start the workflow
        // WorkOrderWorkflow workflow = client.newWorkflowStub(WorkOrderWorkflow.class)
        //workflow.processWorkOrder("resources/workflows/dim-workflow.json");
    }
}
