package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import platform.service.WorkflowStarter;

import java.util.Optional;

@RestController
@RequestMapping("/platform/workflow")
public class PlatformController {
    @Autowired
    private WorkflowStarter workflowStarter;

    @PostMapping("/start")
    public ResponseEntity<String> startWorkflow(@RequestParam String workOrderId,
                                                @RequestParam String assetId,
                                                @RequestBody String workflowDefinition)
    {
        workflowStarter.startWorkflow(workOrderId, assetId, workflowDefinition);
        return ResponseEntity.of(Optional.of("Workflow Started."));
    }
}
