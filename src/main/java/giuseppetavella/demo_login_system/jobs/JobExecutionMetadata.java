package giuseppetavella.demo_login_system.jobs;

import java.util.ArrayList;
import java.util.List;

public class JobExecutionMetadata {
    private final List<String> processedIds = new ArrayList<>();
    
    public JobExecutionMetadata() {
        
    }

    public List<String> getProcessedIds() {
        return processedIds;
    }
    
}
