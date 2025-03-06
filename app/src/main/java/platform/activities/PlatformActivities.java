package platform.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PlatformActivities {
    @ActivityMethod
    boolean register(String taskId);

    @ActivityMethod
    boolean notify(String taskId);
}
