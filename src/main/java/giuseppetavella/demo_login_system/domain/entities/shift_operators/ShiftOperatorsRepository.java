package giuseppetavella.demo_login_system.domain.entities.shift_operators;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftOperatorsRepository extends JpaRepository<ShiftOperator, UUID> {
    
    
    
}
