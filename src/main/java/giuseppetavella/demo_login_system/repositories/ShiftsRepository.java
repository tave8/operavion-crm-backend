package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.clients.ClientAddress;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShiftsRepository extends JpaRepository<Shift, UUID> {

    /**
     * Find shifts of an operator, in a given date,
     * between a time range.
     * 
     * @return shifts that match the above mentioned criteria, where 
     *          a partial overlap exists
     */
    @Query("""
        
        SELECT 
            s
        FROM 
            Shift s
        WHERE 
            s IN (
                SELECT 
                    so.shift
                FROM 
                    ShiftOperator so
                WHERE 
                   so.operator = :operator
                   AND (
                       :inDate >= s.startDate
                       AND :inDate <= s.endDate     
                   )
                   AND (
                       :endTime >= s.startTime
                       AND :startTime <= s.endTime   
                   )
            )      
                
            
    """)
    List<Shift> findShiftsByOperatorInDateBetweenTimes(
        User operator,
        LocalDate inDate,
        LocalTime startTime,
        LocalTime endTime
    );
    

    /**
     * Find shifts of an operator between a date range.
     *
     * @return shifts that match the above mentioned criteria, where 
     *          a partial overlap exists
     */
    @Query("""
        
        SELECT 
            s
        FROM 
            Shift s
        WHERE 
            s IN (
                SELECT 
                    so.shift
                FROM 
                    ShiftOperator so
                WHERE 
                   so.operator = :operator
                   AND (
                       :endDate >= s.startDate
                       AND :startDate <= s.endDate     
                   )
            )      
                
            
    """)
    List<Shift> findShiftsByOperatorBetweenDates(
            User operator,
            LocalDate startDate,
            LocalDate endDate
    );


    /**
     * Find shifts by client address between a date range.
     * 
     * For the shift to be selected, it has to have at least 
     * one operator assigned to it, between the input date range.
     *
     * @return shifts that match the above mentioned criteria, where 
     *          a partial overlap exists
     */
    @Query("""
        
        SELECT 
            DISTINCT s
        FROM 
            Shift s
        WHERE 
            s.clientAddress = :clientAddress
            AND EXISTS (
                SELECT 1
                FROM 
                    ShiftOperator so
                WHERE 
                   so.shift = s
                   AND
                       (:endDate >= s.startDate
                       AND :startDate <= s.endDate)     
            )      
        ORDER BY
            s.startDate,
            s.startTime,
            s.endDate,
            s.endTime
            
    """)
    List<Shift> findShiftsByClientAddressBetweenDates(
            ClientAddress clientAddress,
            LocalDate startDate,
            LocalDate endDate
    );

    

    /**
     * 
     * For an operator to have a conflict in shift:
     * 
     * The date range has an overlap
     * AND these overlapping dates's days include the input days
     * AND there's a time overlap
     * 
     */
    @Query("""
        SELECT s
        FROM Shift s
        WHERE s IN (
            SELECT so.shift
            FROM ShiftOperator so
            WHERE so.operator = :operator
            AND (
                :endDate >= s.startDate
                AND :startDate <= s.endDate
            )
            AND (
                :endTime > s.startTime
                AND :startTime < s.endTime
            )
            AND EXISTS (
                SELECT sd FROM ShiftDay sd
                WHERE sd.shift = so.shift
                AND sd.day IN :days
            )
        )
    """)
    List<Shift> findShiftsWithConflicts(
            @Param("operator") User operator,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("days") List<DayOfWeek> days
    );
    

    /**
     * Find shifts of a company between a date range.
     *
     * @return shifts that match the above mentioned criteria, where 
     *          a partial overlap exists
     */
    @Query("""
        
        SELECT 
            s 
        FROM 
            Shift s
        WHERE 
            s.checklist.company = :company
            AND (
               :endDate >= s.startDate
               AND :startDate <= s.endDate     
           )  
            
    """)
    List<Shift> findShiftsByCompanyBetweenDates(
            Company company,
            LocalDate startDate,
            LocalDate endDate
    );
    
    
    
    /**
     * Find operators that have been assigned to a shift.
     * 
     * @return list of users
     */
    @Query("""
        
        SELECT 
            DISTINCT u
        FROM 
            User u
        WHERE 
            u IN (
                SELECT 
                    so.operator
                FROM 
                    ShiftOperator so
                WHERE 
                    so.shift = :shift
            ) 
                
    """)
    List<User> findOperatorsByShift(
            @Param("shift") Shift shift
    );


    /**
     * Find operators (of a company) with shifts between a date range.
     *
     * 
     * @return list of users
     */
    @Query("""

        SELECT 
            DISTINCT u
        FROM 
            User u
        WHERE 
            u.company = :company
            AND 
                u.role = 'OPERATOR'
            AND 
                u IN (
                    SELECT 
                        so.operator
                    FROM 
                        ShiftOperator so
                    WHERE 
                        so.shift IN (
                            SELECT 
                                s
                            FROM 
                                Shift s
                            WHERE 
                                :endDate >= s.startDate
                                AND :startDate <= s.endDate    
                        )
                ) 

    """)
    List<User> findOperatorsWithShiftsBetweenDates(
            Company company,
            LocalDate startDate,
            LocalDate endDate
    );



    /**
     * Find operators (of a company) without shifts between a date range.
     *
     *
     * @return list of users
     */
    @Query("""

        SELECT 
            DISTINCT u
        FROM 
            User u
        WHERE 
            u.company = :company
            AND 
                u.role = 'OPERATOR'
            AND 
                u NOT IN (
                    SELECT 
                        so.operator
                    FROM 
                        ShiftOperator so
                    WHERE 
                        so.shift IN (
                            SELECT 
                                s
                            FROM 
                                Shift s
                            WHERE 
                                :endDate >= s.startDate
                                AND :startDate <= s.endDate    
                        )
                ) 

    """)
    List<User> findOperatorsWithoutShiftsBetweenDates(
            Company company,
            LocalDate startDate,
            LocalDate endDate
    );



    /**
     * Find operators with shifts between a date range, 
     * with the shift taking place at the given client address.
     *
     *
     * @return list of users
     */
    @Query("""

        SELECT 
            DISTINCT u
        FROM 
            User u
        WHERE 
            u.role = 'OPERATOR'
            AND 
                u IN (
                    SELECT 
                        so.operator
                    FROM 
                        ShiftOperator so
                    WHERE 
                        so.shift IN (
                            SELECT 
                                s
                            FROM 
                                Shift s
                            WHERE 
                                (
                                    :endDate >= s.startDate
                                    AND :startDate <= s.endDate
                                )
                                AND 
                                    s.clientAddress = :clientAddress   
                        )
                ) 

    """)
    List<User> findOperatorsByClientAddressBetweenDates(
            ClientAddress clientAddress,
            LocalDate startDate,
            LocalDate endDate
    );


    
    /**
     * Count how many shifts each operator of the company has, 
     * in this period.
     * Those who have no shifts have their count = 0.
     * 
     * @param company
     * @return
     */
    @Query("""

        SELECT
            u AS user,
            COALESCE(COUNT(s.id), 0) AS total
        FROM
            User u
        LEFT JOIN
            ShiftOperator so ON so.operator = u
        LEFT JOIN
            Shift s ON s = so.shift
            AND :endDate >= s.startDate
            AND :startDate <= s.endDate
        WHERE
            u.role = 'OPERATOR'
            AND u.company = :company
        GROUP BY
            u
        ORDER BY
            COUNT(s.id) DESC,
            u.firstname ASC,
            u.lastname ASC
                    
    """)
    List<Object[]> countShiftsByOperator(
            Company company,
            LocalDate startDate,
            LocalDate endDate
    );

    
    
    /**
     * Is operator busy on given date and time range?
     * 
     * For an operator to be busy on a day between a time range:
     * 
     * The shift for that day for that operator exists, 
     * AND the operator is busy in the input time range for that day,
     * (there exists an overlap between shift time and input time) 
     * AND the day itself is included in the days defined for that shift.
     * 
     *
     */
    // @Query("""
    //    
    //     SELECT 
    //         EXISTS (
    //             SELECT 
    //                 s
    //             FROM 
    //                 Shift s
    //             WHERE 
    //                 s IN (
    //                     SELECT 
    //                         so.shift
    //                     FROM 
    //                         ShiftOperator so
    //                     WHERE 
    //                        so.operator = :operator
    //                        AND (
    //                            :inDate >= s.startDate
    //                            AND :inDate <= s.endDate     
    //                        )
    //                        AND (
    //                            :endTime >= s.startTime
    //                             AND :startTime <= s.endTime 
    //                        )     
    //                 )      
    //                 AND 
    //                     :dayOfDate IN (
    //                         SELECT 
    //                             sd.day
    //                         FROM
    //                             ShiftDay sd
    //                         WHERE
    //                             sd.shift = s        
    //                     )    
    //         )    
    //            
    //        
    // """)
    // boolean isOperatorBusyOn(
    //         User operator,
    //         LocalDate inDate,
    //         LocalTime startTime,
    //         LocalTime endTime,
    //         DayOfWeek dayOfDate
    // );
    

}
