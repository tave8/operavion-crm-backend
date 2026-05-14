package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewShiftSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.NewUserSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ShiftToSendDTO;
import giuseppetavella.demo_login_system.services.ShiftsService;
import jakarta.validation.Payload;
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
    public List<Shift> findShifts(@AuthenticationPrincipal User currentUser,
                                  @RequestParam(value = "from", required = false) LocalDate startDate,
                                  @RequestParam(value = "to", required = false) LocalDate endDate) 
    {
        
        Company company = currentUser.getCompany();
        
        return this.shiftsService.findShiftsBetween(company, startDate, endDate);
        
    }
    
}
