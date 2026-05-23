package giuseppetavella.zero_chiamate.domain.entities.client_address_checklists;

import giuseppetavella.zero_chiamate.domain.entities.checklists.Checklist;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientAddressChecklistsRepository extends JpaRepository<ClientAddressChecklist, UUID> {

    /**
     * The client address has this checklist? 
     * 
     * @param clientAddress
     * @param checklist
     * @return
     */
    @Query("""
    
        SELECT CASE WHEN COUNT(cac) > 0 THEN true ELSE false END
        FROM ClientAddressChecklist cac
        WHERE cac.clientAddress = :clientAddress
          AND cac.checklist = :checklist
            
    """)
    boolean clientAddressHasChecklist(
            ClientAddress clientAddress,
            Checklist checklist
    );
    
}
