package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientsRepository extends JpaRepository<Client, UUID> {
}
