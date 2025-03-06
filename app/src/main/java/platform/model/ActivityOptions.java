package platform.model;

import lombok.Data;

import java.time.Duration;

@Data
public class ActivityOptions {
    private String taskQueue;
    private int startToCloseTimeout;
    private RetryOptions retryOptions;

    public Duration getStartToCloseTimeoutAsDuration() {
        return Duration.ofSeconds(startToCloseTimeout);
    }
}
