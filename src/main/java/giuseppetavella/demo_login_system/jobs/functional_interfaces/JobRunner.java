package giuseppetavella.demo_login_system.jobs.functional_interfaces;

import giuseppetavella.demo_login_system.jobs.JobExecutionItem;
import giuseppetavella.demo_login_system.jobs.JobExecutionResult;

@FunctionalInterface
public interface JobRunner {
    JobExecutionResult processItem(JobExecutionItem item);
}
