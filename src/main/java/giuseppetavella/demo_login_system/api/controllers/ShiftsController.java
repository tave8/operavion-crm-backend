package giuseppetavella.demo_login_system.api.controllers;

import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.helpers.DataValidationHelper;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.domain.entities.shifts.dto.sent.NewShiftSentDTO;
import giuseppetavella.demo_login_system.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.demo_login_system.domain.entities.shifts.dto.to_send.ShiftToSendDTO;
import giuseppetavella.demo_login_system.domain.entities.shifts.ShiftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shifts")
public class ShiftsController {
    
    @Autowired
    private ShiftsService shiftsService;

    /**
     * Add a shift.
     * 
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftToSendDTO addShift(@AuthenticationPrincipal User currentUser,
                                   @RequestBody @Validated NewShiftSentDTO body,
                                   BindingResult validation) 
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        Company company = currentUser.getCompany();
        
        return this.shiftsService.addShift(
                company,
                body
        );
        
    }
    
    
    
    /**
     * Find shifts.
     * 
     * @return
     */
    @GetMapping
    public List<ShiftToSendDTO> findShifts(@AuthenticationPrincipal User currentUser,
                                  @RequestParam(value = "from", required = false) LocalDate startDate,
                                  @RequestParam(value = "to", required = false) LocalDate endDate) 
    {

        DataValidationHelper.requireValidRange(startDate, endDate);
        
        Company company = currentUser.getCompany();
        
        return this.shiftsService.findShiftsBetweenDTO(company, startDate, endDate);
        
    }

    /**
     * Find operators by filtering shifts.
     *
     * @return
     */
    @GetMapping("/operators")
    public List<ProfileToSendDTO> findOperators(@AuthenticationPrincipal User currentUser,
                                               @RequestParam(value = "from", required = false) LocalDate startDate,
                                               @RequestParam(value = "to", required = false) LocalDate endDate,
                                                @RequestParam(value = "hasShifts", defaultValue = "true") Boolean hasShifts)
    {

        DataValidationHelper.requireValidRange(startDate, endDate);

        Company company = currentUser.getCompany();
        
        if(hasShifts) {
            return this.shiftsService.findOperatorsWithShiftsBetweenDatesDTO(company, startDate, endDate);
        }
        
        return this.shiftsService.findOperatorsWithoutShiftsBetweenDatesDTO(company, startDate, endDate);

    }
    

    /**
     * Find shift.
     *
     * @return
     */
    // @GetMapping
    // public List<ShiftToSendDTO> findShifts(@AuthenticationPrincipal User currentUser,
    //                                        @RequestParam(value = "from", required = false) LocalDate startDate,
    //                                        @RequestParam(value = "to", required = false) LocalDate endDate)
    // {
    //
    //     Company company = currentUser.getCompany();
    //
    //     return this.shiftsService.findShiftsBetweenDTO(company, startDate, endDate);
    //
    // }
    
}
