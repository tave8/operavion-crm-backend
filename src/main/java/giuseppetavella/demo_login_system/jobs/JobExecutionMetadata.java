package giuseppetavella.demo_login_system.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobExecutionMetadata {
    private final List<String> processedItemIds = new ArrayList<>();
    private final List<String> failedItemIds = new ArrayList<>();
    private final List<String> skippedItemIds = new ArrayList<>();
    private final Map<String, Object> extra = new HashMap<>();
    
    public JobExecutionMetadata() {
        
    }

    public List<String> getFailedItemIds() {
        return failedItemIds;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public List<String> getProcessedItemIds() {
        return processedItemIds;
    }

    public List<String> getSkippedItemIds() {
        return skippedItemIds;
    }
}
