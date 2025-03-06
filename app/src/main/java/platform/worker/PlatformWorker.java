package platform.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.stereotype.Component;
import platform.activities.PlatformActivitiesImpl;
import platform.activities.WorkOrderActivitiesImpl;
import platform.workflow.WorkOrderWorkflowImpl;

//@Component
public class PlatformWorker {
    private final WorkflowClient workflowClient;

    public PlatformWorker(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    public void start() {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker("platform-task-queue");
        worker.registerWorkflowImplementationTypes(WorkOrderWorkflowImpl.class);
        worker.registerActivitiesImplementations(new WorkOrderActivitiesImpl(), new PlatformActivitiesImpl());
        factory.start();
        System.out.println("Platform worker started for platform-task-queue");
    }
}
