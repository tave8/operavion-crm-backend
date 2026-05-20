package giuseppetavella.demo_login_system.entities;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
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

    @Column(name = "extracted_text", nullable = false, columnDefinition = "TEXT")
    private String extractedText;
    
    @Column(name = "processed_at", nullable = false)
    private OffsetDateTime processedAt;
    
    protected ContractExpectation() {}
    
    public ContractExpectation(ClientAddress clientAddress,
                               String extractedText) 
    {
        this.clientAddress = clientAddress;
        this.setExtractedText(extractedText);
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
