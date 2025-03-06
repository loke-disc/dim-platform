package platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import platform.entity.Task;
import platform.model.WorkflowDefinition;

import java.util.List;
import java.util.stream.Collectors;

//@Service
public class WorkflowDefinitionParser {

    public WorkflowDefinition parseWorkflowDefinition(String workflowDefinitionJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(workflowDefinitionJSON, WorkflowDefinition.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse workflow definition", e);
        }
    }

    public List<Task> extractTasks(String workflowDefinitionJSON) {
        WorkflowDefinition workflowDefinition = parseWorkflowDefinition(workflowDefinitionJSON);
        return workflowDefinition.getTasks().stream().map(taskDefinition -> {
                                                        Task task = new Task();
                                                        task.setTaskId(taskDefinition.getTaskId());
                                                        task.setDescription(taskDefinition.getDescription());
                                                        task.setStatus("New");
                                                        return task;
        }).collect(Collectors.toList());
    }
}
