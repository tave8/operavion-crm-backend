package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.entities.shifts.Shift;
import giuseppetavella.demo_login_system.services.ShiftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shifts")
public class ShiftsController {
    
    @Autowired
    private ShiftsService shiftsService;

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
