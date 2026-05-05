package giuseppetavella.demo_login_system.jobs;

import java.util.UUID;

public class JobExecutionItem<T> {
    
    private final T item;
    private final UUID itemId;
    
    public JobExecutionItem(T item, UUID itemId) {
        this.item = item;    
        this.itemId = itemId;
    }

    public T getItem() {
        return item;
    }

    public UUID getItemId() {
        return itemId;
    }

    @Override
    public String toString() {
        return "JobExecutionItem{" +
                "item=" + item +
                '}';
    }
}
