package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Client;
import giuseppetavella.demo_login_system.entities.ClientAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientAddressesRepository extends JpaRepository<ClientAddress, UUID> {

    /**
     * Find addresses of the given client.
     * 
     * @param client
     * @param pageable
     * @return
     */
    @Query("SELECT c FROM ClientAddress c WHERE c.client = :client")
    Page<ClientAddress> findClientAddresses(
            Client client,
            Pageable pageable
    );
    
}
