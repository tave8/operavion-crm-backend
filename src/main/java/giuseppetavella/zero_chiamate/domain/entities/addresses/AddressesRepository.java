package giuseppetavella.zero_chiamate.domain.entities.addresses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressesRepository extends JpaRepository<giuseppetavella.zero_chiamate.domain.entities.addresses.Address, UUID> {
}
