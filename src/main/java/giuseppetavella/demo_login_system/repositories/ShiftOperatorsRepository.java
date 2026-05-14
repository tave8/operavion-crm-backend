package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.shifts.ShiftOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftOperatorsRepository extends JpaRepository<ShiftOperator, UUID> {
    
    
    
}
