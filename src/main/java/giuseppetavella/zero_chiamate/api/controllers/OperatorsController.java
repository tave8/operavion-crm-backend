package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.UserRole;
import giuseppetavella.zero_chiamate.helpers.AuthorizationHelper;
import giuseppetavella.zero_chiamate.helpers.DataValidationHelper;
import giuseppetavella.zero_chiamate.domain.entities.shifts.dto.to_send.OperatorShiftConflictsToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.shifts.dto.to_send.OperatorShiftAvailabilityToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.shifts.dto.to_send.ShiftToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/operators")
public class OperatorsController {
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private UsersService usersService;

    /**
     * Find all operators of company. 
     * 
     * @param currentUser
     * @return
     */
    @GetMapping
    public List<ProfileToSendDTO> findOperators(@AuthenticationPrincipal User currentUser)
    {
        Company company = currentUser.getCompany();
        
        return this.usersService.findUsersByRoleDTO(company, UserRole.OPERATOR);
    }
    

    /**
     * Find shifts by operator between dates.
     */
    @GetMapping("/{operatorId}/shifts")
    public List<ShiftToSendDTO> findShiftsByOperator(@AuthenticationPrincipal User currentUser,
                                                     @PathVariable UUID operatorId,
                                                     @RequestParam(value = "from", required = false) LocalDate startDate,
                                                     @RequestParam(value = "to", required = false) LocalDate endDate)
    {
        DataValidationHelper.requireValidRange(startDate, endDate);
        
        Company company = currentUser.getCompany();
        
        User operator = this.usersService.findById(operatorId);

        AuthorizationHelper.requireSameCompany(company, operator.getCompany());
        
        AuthorizationHelper.requireUserOperator(operator);
        
        return this.shiftsService.findShiftsByOperatorBetweenDatesDTO(
                operator,
                startDate,
                endDate
        );  
    }


    /**
     * Find shifts by current operator, between dates.
     */
    @GetMapping("/me/shifts")
    public List<ShiftToSendDTO> findMyShifts(@AuthenticationPrincipal User currentUser,
                                             @RequestParam(value = "from", required = false) LocalDate startDate,
                                             @RequestParam(value = "to", required = false) LocalDate endDate)
    {
        DataValidationHelper.requireValidRange(startDate, endDate);

        AuthorizationHelper.requireUserOperator(currentUser);

        return this.shiftsService.findShiftsByOperatorBetweenDatesDTO(
                currentUser,
                startDate,
                endDate
        );
    }

    
    /**
     * Get the operator availability.
     */
    @GetMapping("/{operatorId}/shifts/availability")
    public OperatorShiftAvailabilityToSendDTO getOperatorAvailability(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID operatorId,
            // this query param is required
            @RequestParam(value = "date") LocalDate inDate,
            // these query params are optional
            @RequestParam(value = "fromTime", required = false) LocalTime fromTime,
            @RequestParam(value = "toTime", required = false) LocalTime toTime
    )
    {

        DataValidationHelper.requireValidRange(fromTime, toTime);
        
        Company company = currentUser.getCompany();
        
        User operator = this.usersService.findById(operatorId);
        
        AuthorizationHelper.requireSameCompany(company, operator.getCompany());
        
        AuthorizationHelper.requireUserOperator(operator);
        
        // bug fix: i had passed currentUser instead of operator
        // learned: check if the passed user is truly an operator
        // you might not get any resultset back and wonder why,
        // when it could be that the user is not even an operator
        // and non-operators don't have shifts
        return this.shiftsService.getOperatorAvailability(
                operator,
                inDate,
                fromTime,
                toTime
        );
        
    }



    /**
     * Get the operator conflicts.
     */
    @GetMapping("/{operatorId}/shifts/conflicts")
    public OperatorShiftConflictsToSendDTO findOperatorConflicts(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID operatorId,
            // required
            @RequestParam(value = "from") LocalDate from,
            @RequestParam(value = "to") LocalDate to,
            @RequestParam(value = "days") List<DayOfWeek> days,
            // optional
            @RequestParam(value = "fromTime", required = false) LocalTime fromTime,
            @RequestParam(value = "toTime", required = false) LocalTime toTime
    )
    {

        
        DataValidationHelper.requireValidRange(from, to);
        
        DataValidationHelper.requireValidRange(fromTime, toTime);

        Company company = currentUser.getCompany();

        User operator = this.usersService.findById(operatorId);

        AuthorizationHelper.requireSameCompany(company, operator.getCompany());

        AuthorizationHelper.requireUserOperator(operator);

        return this.shiftsService.findOperatorConflicts(
                operator,
                from,
                to,
                fromTime,
                toTime,
                days
        );

    }


}
