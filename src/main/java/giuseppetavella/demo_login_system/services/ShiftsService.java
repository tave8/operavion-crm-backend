package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import giuseppetavella.demo_login_system.repositories.ShiftsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftsService {

    @Autowired
    private ShiftsRepository shiftsRepository;

    /**
     * Find all shifts between two dates.
     */
    public List<Shift> findShiftsBetween(Company company, LocalDate startDate, LocalDate endDate) {
        return shiftsRepository.findShiftsBetween(company, startDate, endDate);
    }

    /**
     * Find all shifts of an operator.
     */
    // public List<Shift> findShiftsByOperator(User operator) {
    //     return shiftsRepository.findShiftsByOperator(operator);
    // }
    //
    // /**
    //  * Find all shifts of an operator between two dates.
    //  */
    // public List<Shift> findShiftsByOperatorBetween(User operator, LocalDate startDate, LocalDate endDate) {
    //     return shiftsRepository.findShiftsByOperatorBetween(operator, startDate, endDate);
    // }
    //
    // /**
    //  * Find all operators that have shifts between two dates.
    //  */
    // public List<User> findOperatorsBetween(LocalDate startDate, LocalDate endDate) {
    //     return shiftsRepository.findOperatorsBetween(startDate, endDate);
    // }
    //
    // /**
    //  * Find all operators that have no shifts between two dates.
    //  */
    // public List<User> findOperatorsWithoutShiftsBetween(LocalDate startDate, LocalDate endDate) {
    //     return shiftsRepository.findOperatorsWithoutShiftsBetween(startDate, endDate);
    // }

}