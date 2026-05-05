package giuseppetavella.demo_login_system.jobs;

import java.util.UUID;

public class JobExecutionItem {
    
    private final UUID itemId;
    
    public JobExecutionItem(UUID itemId) {
        this.itemId = itemId;    
    }

    public UUID getItemId() {
        return itemId;
    }

    @Override
    public String toString() {
        return "JobExecutionItem{" +
                "itemId=" + itemId +
                '}';
    }
}
