package platform.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface GenericActivities {
    @ActivityMethod
    boolean executeTask(String taskName, String taskId);
}
