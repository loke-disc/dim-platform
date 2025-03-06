package platform.activities;

import org.springframework.stereotype.Service;

@Service
public class PlatformActivitiesImpl implements PlatformActivities {
    @Override
    public boolean register(String taskId) {
        System.out.println("Process register task");
        return true;
    }

    @Override
    public boolean notify(String taskId) {
        System.out.println("Process notify task");
        return true;
    }
}
