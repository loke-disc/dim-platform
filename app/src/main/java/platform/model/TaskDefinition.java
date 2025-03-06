package platform.model;

import lombok.Data;

import java.util.List;

@Data
public class TaskDefinition {
    private String taskId;
    private String description;
    private String serviceType;
    private String className;
    private String method;
    private List<String> conditions;
    private int order;
    private ActivityOptions activityOptions;
}
