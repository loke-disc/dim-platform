package platform.model;

import lombok.Data;

import java.util.List;

@Data
public class WorkflowDefinition {
    private String workflowId;
    private List<TaskDefinition> tasks;
}
