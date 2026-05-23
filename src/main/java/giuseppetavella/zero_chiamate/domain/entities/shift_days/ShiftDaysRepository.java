package giuseppetavella.zero_chiamate.domain.entities.shift_days;

import giuseppetavella.zero_chiamate.domain.entities.shifts.Shift;
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
