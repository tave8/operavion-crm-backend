package giuseppetavella.demo_login_system.jobs;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents the execution of a job.
 */
@Entity
@Table(name = "job_executions")
public class JobExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobName jobName;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobExecutionState state;
    
    @Column(name = "last_processed_item_id", nullable = false)
    private UUID lastProcessedItemId;
    
    @Column(name = "last_processed_item_created_at", nullable = false)
    private OffsetDateTime lastProcessedItemCreatedAt;
    
    // generated automatically at the DB level
    @Column(name = "started_at", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime startedAt;
    
    // you can only set this once and never change it 
    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;
    
    @Column(nullable = false)
    private String message;
    
    protected JobExecution() {}

    /**
     * <h1>Create a new job execution instance</h1>
     * 
     * The initial state is PENDING.
     * 
     * <h1>About the meaning of "last processed item"</h1>
     * 
     * When we create a new job execution, we have just started 
     * processing this item. This item that we are processing is not any item. 
     * 
     * From the point of view of the operation that gets items to process,
     * it is indeed the next item. 
     * 
     * From the point of view of this job execution being instantiated, 
     * it is the current item.
     * 
     * From the point of view of a database table or even the entire 
     * job, it is effectively the last processed time at that point in time.
     * 
     * I've chosen the latter semantics.
     * 
     * 
     * @param jobName the job to which this job execution belongs
     * @param lastProcessedItemId the ID of the last processed item
     * @param lastProcessedItemCreatedAt the timestamp of the last processed item
     */
    public JobExecution(JobName jobName, 
                        UUID lastProcessedItemId, 
                        OffsetDateTime lastProcessedItemCreatedAt) 
    {
        this.jobName = jobName;
        this.lastProcessedItemId = lastProcessedItemId;
        this.lastProcessedItemCreatedAt = lastProcessedItemCreatedAt;
        this.setState(JobExecutionState.PENDING);
        this.setMessage("");
    }



    /**
     * You may call this method only once, and that is 
     * when you finish this job execution.
     * 
     * @throws JobExecutionException if you try to call this method
     *  when the finish time of this job execution was already set.
     */
    public void finish() throws JobExecutionException
    {
        boolean alreadySet = this.getFinishedAt() != null;
        
        if(alreadySet) {
            throw new JobExecutionException( 
                    this.getJobName(),
                    "You cannot set the finish time of a job execution again. " 
                            +"The 'finishedAt' attribute for this job execution "
                            +"was already set with value '"+this.getFinishedAt()+"' ."
            );    
        }
        
        this.finishedAt = OffsetDateTime.now();
    }
    
    /**
     * 
     * @param desiredState the state that is desired to be the new state
     * @throws JobExecutionException if the desired state cannot be set 
     *  based on the current state
     */
    public void setState(JobExecutionState desiredState) throws JobExecutionException 
    {
        // restrictions on the state you're trying to set
        JobExecutionState currState = this.getState();
        
        // map: current state -> next possible states
        Map<JobExecutionState, List<JobExecutionState>> states = Map.of(
                JobExecutionState.PENDING, List.of(JobExecutionState.SUCCESS, JobExecutionState.FAILED),
                JobExecutionState.SUCCESS, List.of(),
                JobExecutionState.FAILED, List.of()
        );
        
        // if the desired state is not in the list of possible states,
        // it means you cannot set this desired state 
        // to be the new state
        boolean canSetNewState = states.get(currState).contains(desiredState);
        
        if(!canSetNewState) {
            throw new JobExecutionException(
                    this.getJobName(),
                    "Cannot transition from current state " + currState + " to desired state " + desiredState
            );
        }
        
        this.state = desiredState;
    }

    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }
    
    public JobName getJobName() {
        return jobName;
    }



    public Long getId() {
        return id;
    }


    public OffsetDateTime getLastProcessedItemCreatedAt() {
        return lastProcessedItemCreatedAt;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getLastProcessedItemId() {
        return lastProcessedItemId;
    }


    public OffsetDateTime getStartedAt() {
        return startedAt;
    }


    public JobExecutionState getState() {
        return state;
    }


    @Override
    public String toString() {
        return "JobExecution{" +
                "id=" + id +
                ", jobName=" + jobName +
                ", state=" + state +
                ", startedAt=" + startedAt +
                ", finishedAt=" + finishedAt +
                ", lastProcessedItemCreatedAt=" + lastProcessedItemCreatedAt +
                ", lastProcessedItemId=" + lastProcessedItemId +
                ", message='" + message + '\'' +
                '}';
    }
}
