package platform.model;

import lombok.Data;

@Data
public class RetryOptions {
    private int maximumAttempts;
}
