package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.clients.Client;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientAddressesRepository extends JpaRepository<ClientAddress, UUID> {

    /**
     * Find client addresses of company.
     * Search by address name OR client legal name.
     *
     * @return
     */
    @Query(""" 
            
        SELECT c 
        FROM ClientAddress c 
        WHERE 
            c.client.company = :company 
            AND (
                :searchQueryPattern IS NULL 
                OR (
                     (LOWER(c.addressName) LIKE :searchQueryPattern)
                     OR (LOWER(c.client.legalName) LIKE :searchQueryPattern)
                )
            )
     """)
    Page<ClientAddress> findClientAddressessByCompany(
            Company company,
            String searchQueryPattern,
            Pageable pageable
    );



    /**
     * Find addresses of the given client.
     * 
     * @param client
     * @param pageable
     * @return
     */
    @Query("SELECT c FROM ClientAddress c WHERE c.client = :client")
    Page<ClientAddress> findAddressesByClient(
            Client client,
            Pageable pageable
    );
    
}
