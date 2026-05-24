package giuseppetavella.zero_chiamate.api.controllers.auth;


// import giuseppetavella.demo_login_system.domain.entities.auth.AuthService;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.LoginSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.OperatorLoginSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.SignupSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.to_send.AfterLoginDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.to_send.AfterSignupDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    

    // ************************************
    // SIGNUP & LOGIN
    // ************************************
    
    /**
     * Login a company + user admin.
     */
    @PostMapping("/login")
    public AfterLoginDTO login(@RequestBody @Validated LoginSentDTO body, 
                               BindingResult validation) 
    {
        
        PayloadValidationHelper.requireNoErrors(validation);
        
        return authService.login(body);
    }


    /**
     * Login an operator.
     */
    @PostMapping("/login-operator")
    public AfterLoginDTO loginOperator(@RequestBody @Validated OperatorLoginSentDTO body,
                                        BindingResult validation)
    {

        PayloadValidationHelper.requireNoErrors(validation);

        return authService.loginOperator(body);
    }


    /**
     * Sign up a user.
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AfterSignupDTO signup(@RequestBody @Validated SignupSentDTO body,
                                   BindingResult validation) 
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        return this.authService.signup(body);

    }


    
    

}