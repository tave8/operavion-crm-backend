package giuseppetavella.demo_login_system.domain.entities.addresses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressesRepository extends JpaRepository<giuseppetavella.demo_login_system.domain.entities.addresses.Address, UUID> {
}
