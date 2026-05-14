package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.payloads.in_response.ShiftToSendDTO;
import giuseppetavella.demo_login_system.services.ShiftsService;
import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Find shifts by operator.
     */
    @GetMapping("/{operatorId}/shifts")
    public List<ShiftToSendDTO> findShiftsByOperator(@AuthenticationPrincipal User currentUser,
                                                     @PathVariable UUID operatorId)
    {
          
        Company company = currentUser.getCompany();
        
        User operator = this.usersService.findById(operatorId);

        AuthorizationHelper.requireSameCompany(company, operator.getCompany());
        
        AuthorizationHelper.requireUserOperator(operator);
        
        return this.shiftsService.findShiftsByOperatorDTO(operator);  
    }
    
    
}
