package giuseppetavella.demo_login_system.entities;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.enums.internal.ContractExpectationState;
import giuseppetavella.demo_login_system.exceptions.ContractExpectationException;
import giuseppetavella.demo_login_system.helpers.DataValidationHelper;
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
    
    @Column(name = "expectations", nullable = false, columnDefinition = "TEXT")
    private String expectations;
    
    @Column(name = "processed_at", nullable = false)
    private OffsetDateTime processedAt;
    
    protected ContractExpectation() {}
    
    public ContractExpectation(ClientAddress clientAddress) 
    {
        this.clientAddress = clientAddress;
        // when we first instantiate a contraxt expectation,
        // there's no extracted text, because the operation
        // occurs in the background
        this.setExpectations("");
        // on instantiation, the contract cannot  be processed
        this.state = ContractExpectationState.PENDING;
        this.processedAt = OffsetDateTime.now();
    }


    /**
     * <pre>
     * no state      ->  PENDING
     * PENDING       ->  SUCCESS | FAILED
     * SUCCESS       ->  
     * FAILED        ->  PENDING
     * </pre>
     */
    public void setState(ContractExpectationState desiredState) {

        ContractExpectationState currentState = this.getState();

        // instance has no state yet if current state is null
        boolean noStateYet = currentState == null;
        
        // first states
        List<ContractExpectationState> firstStates = List.of(ContractExpectationState.PENDING);
        
        // state map (if current state exists)
        Map<ContractExpectationState, List<ContractExpectationState>> stateMap = Map.of(
                ContractExpectationState.PENDING, List.of(ContractExpectationState.SUCCESS, ContractExpectationState.FAILED),
                ContractExpectationState.SUCCESS, List.of(),
                // if processing failed, we can still re-process it
                ContractExpectationState.FAILED, List.of(ContractExpectationState.PENDING)
        );

        // check if valid state transition
        DataValidationHelper.requireValidStateTransition(
                ContractExpectationState.class,
                currentState,
                desiredState,
                firstStates,
                stateMap,
                noStateYet,
                "contract expectation"
        );

        
        this.state = desiredState;
        
    }

    public ContractExpectationState getState() {
        return state;
    }

    public ClientAddress getClientAddress() {
        return clientAddress;
    }
    

    public String getExpectations() {
        return expectations;
    }

    public void setExpectations(String expectations) {
        this.expectations = expectations;
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
                ", processedAt=" + processedAt +
                '}';
    }
}
