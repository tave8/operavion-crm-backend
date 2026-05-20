package giuseppetavella.demo_login_system.payloads.in_response;

/**
 * This DTO is to be used when contract expectations
 * are extracted. If you need to send a DB entity instance,
 * use the other DTO (ContractExpectationToSendDTO).
 */
public class ExtractedContractExpectationsToSendDTO {
    
    private final String extractedText;
    
    public ExtractedContractExpectationsToSendDTO(String extractedText) {
        this.extractedText = extractedText;
    }


    public String getExtractedText() {
        return extractedText;
    }
}
