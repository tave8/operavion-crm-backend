package giuseppetavella.demo_login_system.entities;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.enums.internal.ContractExpectationState;
import giuseppetavella.demo_login_system.exceptions.ContractExpectationException;
import giuseppetavella.demo_login_system.job_library.enums.JobExecutionState;
import giuseppetavella.demo_login_system.job_library.exceptions.JobExecutionException;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "contract_expectations")
public class ContractExpectation {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @OneToOne
    @JoinColumn(name = "client_address_id", unique = true, nullable = false)
    private ClientAddress clientAddress;
    
    // the state of the contract expectation 
    // refers to whether this contract was processed or not
    @Enumerated(EnumType.STRING)
    private ContractExpectationState state;
    
    @Column(name = "extracted_text", nullable = false, columnDefinition = "TEXT")
    private String extractedText;
    
    @Column(name = "processed_at", nullable = false)
    private OffsetDateTime processedAt;
    
    protected ContractExpectation() {}
    
    public ContractExpectation(ClientAddress clientAddress) 
    {
        this.clientAddress = clientAddress;
        // when we first instantiate a contraxt expectation,
        // there's no extracted text, because the operation
        // occurs in the background
        this.setExtractedText("");
        // on instantiation, the contract cannot  be processed
        this.state = ContractExpectationState.PENDING;
        this.processedAt = OffsetDateTime.now();
    }

    public void setState(ContractExpectationState desiredState) {

        ContractExpectationState currState = this.getState();

        boolean isFirstState = currState == null;

        // if this is the first state assigned, it can only be pending
        if(isFirstState) {

            boolean isNewStatePending = desiredState.equals(ContractExpectationState.PENDING);

            if(isNewStatePending) {
                this.state = desiredState;
                return;
            }

            throw new ContractExpectationException(
                    "The first state of a job contract expectations "
                            +"can only be PENDING, got '" + desiredState + "' instead. "
            );

        }


        // map: current state -> next possible states
        Map<ContractExpectationState, List<ContractExpectationState>> states = Map.of(
                ContractExpectationState.PENDING, List.of(ContractExpectationState.SUCCESS, ContractExpectationState.FAILED),
                ContractExpectationState.SUCCESS, List.of(),
                ContractExpectationState.FAILED, List.of()
        );

        // if the desired state is not in the list of possible states,
        // it means you cannot set this desired state 
        // to be the new state
        boolean canSetNewState = states.get(currState).contains(desiredState);

        if(!canSetNewState) {
            throw new ContractExpectationException(
                    "Cannot set new state, because you cannot transition "
                            +"from current state " + currState + " to desired state " + desiredState + ". "
            );
        }

        this.state = desiredState;
        
    }

    public ContractExpectationState getState() {
        return state;
    }

    public ClientAddress getClientAddress() {
        return clientAddress;
    }
    

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public UUID getId() {
        return id;
    }
    

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    @Override
    public String toString() {
        return "ContractExpectation{" +
                "clientAddress=" + clientAddress +
                ", id=" + id +
                ", extractedText='" + extractedText + '\'' +
                ", processedAt=" + processedAt +
                '}';
    }
}
