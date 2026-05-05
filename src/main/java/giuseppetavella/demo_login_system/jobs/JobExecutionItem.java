package giuseppetavella.demo_login_system.jobs;

public class JobExecutionItem<T> {
    
    private final T item;
    
    public JobExecutionItem(T item) {
        this.item = item;    
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "JobExecutionItem{" +
                "item=" + item +
                '}';
    }
}
