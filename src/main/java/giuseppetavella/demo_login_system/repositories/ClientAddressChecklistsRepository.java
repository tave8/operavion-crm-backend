package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Checklist;
import giuseppetavella.demo_login_system.entities.ClientAddress;
import giuseppetavella.demo_login_system.entities.ClientAddressChecklist;
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
