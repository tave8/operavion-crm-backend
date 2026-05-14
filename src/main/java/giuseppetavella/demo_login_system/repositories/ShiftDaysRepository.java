package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.shifts.Shift;
import giuseppetavella.demo_login_system.entities.shifts.ShiftDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftDaysRepository extends JpaRepository<ShiftDay, UUID> {

    /**
     * Find shift days by shift.
     * @param shift
     * @return
     */
    @Query("""
        
        SELECT sd
        FROM ShiftDay sd
        WHERE sd.shift = :shift
        
    """)
    List<ShiftDay> findByShift(Shift shift);
    
}
