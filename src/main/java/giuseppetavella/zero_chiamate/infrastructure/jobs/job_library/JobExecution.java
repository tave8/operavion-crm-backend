package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library;

import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.enums.JobExecutionState;
import giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.JobName;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions.JobExecutionException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    
    // job name is a string and not an enum
    // because i want flexibility and not having
    // to touch the DB every time i add a new job name
    // however, i get safety at compile-time,
    // by passing it as an enum, and only in the method
    // i get the string of the enum
    @Column(name = "job_name", nullable = false)
    private String jobName;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobExecutionState state;
    
    @Column(name = "last_processed_item_id", nullable = false)
    private UUID lastProcessedItemId;
    
    // generated automatically at the DB level
    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;
    
    // you can only set this once and never change it 
    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

    /**
     * Add custom metadata in the "extra" field inside metadata.
     * The field in DB will have json.
     */
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JobExecutionMetadata metadata;
    
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    protected JobExecution() {}

    /**
     * <h1>Create a new job execution instance</h1>
     * 
     * The initial state is INCOMPLETE.
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
     */
    public JobExecution(@NotNull JobName jobName, 
                        @NotNull UUID lastProcessedItemId) 
    {
        
        // the job name saved in DB will actually be a string,
        // because i want flexibility for now
        this.jobName = jobName.name();
        this.lastProcessedItemId = lastProcessedItemId;
        this.startedAt = OffsetDateTime.now();
        this.retryCount = 0;
        this.metadata = new JobExecutionMetadata();
        this.setState(JobExecutionState.INCOMPLETE);
        this.setMessage("");
        
    }
    
    public void incrementRetryCount() {
         this.retryCount += 1;  
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    /**
     * Set the state of this job execution, and finish it (sets the 
     * finishedAt attribute to this time).
     */
    public void setStateAndFinish(JobExecutionState desiredState, 
                                  @Nullable String message) throws JobExecutionException
    {
        
        this.setState(desiredState);
        this.finish();
        this.concatenateMessage(message);
    }

    
    public void setStateAndFinish(JobExecutionState desiredState) throws JobExecutionException
    {
        
        this.setStateAndFinish(desiredState, null);
        
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
                    this.getJobName().toString(),
                    "You cannot set the finish time of a job execution again. " 
                            +"The 'finishedAt' attribute for this job execution "
                            +"was already set with value '"+this.getFinishedAt()+"'. "
                            +"Job execution had ID " + this.getId()
            );    
        }
        
        this.finishedAt = OffsetDateTime.now();
    }
    
    
    /**
     * In this setter we apply restrictions on the new state
     * that you're trying to set.
     * 
     * 
     * <pre>
     * CURR STATE   |  NEXT POSSIBLE STATES
     * ------------------------------------
     *   none            INCOMPLETE
     *   INCOMPLETE      SUCCESS, FAILED, ABANDONED
     *   SUCCESS          
     *   FAILED
     * 
     * </pre>
     * 
     * 
     * @param desiredState the state that is desired to be the new state
     * @throws JobExecutionException if the desired state cannot be set 
     *  based on the current state
     */
    public void setState(JobExecutionState desiredState) throws JobExecutionException 
    {
        
        JobExecutionState currState = this.getState();
        
        boolean isFirstState = currState == null;
        
        // if this is the first state assigned, it can only be incomplete
        if(isFirstState) {
            
            boolean isNewStateIncomplete = desiredState.equals(JobExecutionState.INCOMPLETE);
            
            if(isNewStateIncomplete) {
                this.state = desiredState;
                return;  
            } 
            
            throw new JobExecutionException(
                    this.getJobName().name(),
                    "The first state of a job execution "
                            +"can only be INCOMPLETE, got '" + desiredState + "' instead. "
                            +"Job execution had ID " + this.getId()
            );
            
        }
        
        
        // map: current state -> next possible states
        Map<JobExecutionState, List<JobExecutionState>> states = Map.of(
                JobExecutionState.INCOMPLETE, List.of(JobExecutionState.SUCCESS, JobExecutionState.FAILED, JobExecutionState.ABANDONED),
                JobExecutionState.SUCCESS, List.of(),
                JobExecutionState.FAILED, List.of()
        );
        
        // if the desired state is not in the list of possible states,
        // it means you cannot set this desired state 
        // to be the new state
        boolean canSetNewState = states.get(currState).contains(desiredState);
        
        if(!canSetNewState) {
            throw new JobExecutionException(
                    this.getJobName().name(),
                    "Cannot set new state, because you cannot transition " 
                            +"from current state " + currState + " to desired state " + desiredState + ". "
                            + "Job execution had ID " + this.getId()
            );
        }
        
        this.state = desiredState;
    }

    
    /**
     * 
     * 
     */
    public JobName getJobName() {
        try {
            
            return JobName.valueOf(jobName);
            
        } catch(IllegalArgumentException ex) {
            
            throw new JobExecutionException(
                    this.jobName,
                    "While parsing the job name '" + this.jobName + "' into an enum constant, "
                            +"no matching enum constant was found. This means that "
                            +"this job name was deleted or modified. "
                            +"This error should be handled better. " 
                            + "Job execution had ID " + this.getId() 
            );
            
        }
    }

    /**
     * Concatenate the message with the current message
     */
    public void concatenateMessage(@Nullable String message) {
        // if a message is provided, it gets added to the current message
        if(message != null) {
            String currMessage = this.getMessage();

            // if the current message is empty
            if(currMessage.isBlank()) {
                // just set the new message as is
                this.setMessage(message);

            } else {

                // concatenate the existing message with the new message
                String newMessage = currMessage + " | " + message;
                this.setMessage(newMessage);

            }
        }
    }

    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }
    

    public Long getId() {
        return id;
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

    public JobExecutionMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "JobExecution{" +
                "id=" + id +
                ", jobName=" + jobName +
                ", state=" + state +
                ", startedAt=" + startedAt +
                ", finishedAt=" + finishedAt +
                ", lastProcessedItemId=" + lastProcessedItemId +
                ", message='" + message + '\'' +
                '}';
    }
}
