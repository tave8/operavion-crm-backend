package giuseppetavella.demo_login_system.api.controllers;


// import giuseppetavella.demo_login_system.domain.entities.auth.AuthService;
import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.exceptions.EmailVerificationException;
import giuseppetavella.demo_login_system.exceptions.ForgotPasswordVerificationException;
import giuseppetavella.demo_login_system.exceptions.InvalidUUIDStringException;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.LoginSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.OperatorLoginSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.SignupSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.forgot_password.ForgotPasswordNewPasswordSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.forgot_password.ForgotPasswordRequestWithEmailSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.forgot_password.VerifyForgotPasswordCodeSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.reset_password.ResetPasswordOldPasswordSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.to_send.AfterLoginDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.to_send.AfterSignupDTO;
import giuseppetavella.demo_login_system.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.to_send.forgot_password.ForgotPasswordToSendDTO;
import giuseppetavella.demo_login_system.domain.entities.users.UsersService;
import giuseppetavella.demo_login_system.domain.business.auth.AuthService;
import giuseppetavella.demo_login_system.domain.business.auth.EmailVerificationService;
import giuseppetavella.demo_login_system.domain.business.auth.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private EmailVerificationService emailVerificationService;
    
    @Autowired
    private ForgotPasswordService forgotPasswordService;



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

    // ************************************
    // RESET PASSWORD AT FIRST LOGIN
    // ************************************
    
    /**
     * Reset password at first login.
     */
    @PostMapping("/reset-password-first-login")
    public ProfileToSendDTO resetPasswordAtFirstLoging(@RequestBody @Validated ResetPasswordOldPasswordSentDTO body,
                                                        @AuthenticationPrincipal User currentUser,
                                                        BindingResult validation)
    {

        PayloadValidationHelper.requireNoErrors(validation);

        return this.usersService.resetPasswordAtFirstLogin(currentUser, body);

    }
    
    

    
    // ************************************
    // VERIFY USER'S EMAIL
    // ************************************

    
    /**
     * Verify if the code is valid.
     * If yes, the email of the account associated with this code, 
     * will be marked as verified.
     * 
     * Returns a simple html page saying that email is now verified.
     */
    @GetMapping("/verify-email/{code}")
    public String verifyEmail(@PathVariable String code) {
        
        try {
            
            this.emailVerificationService.verifyEmailVerificationCode(code);
            
        } catch (EmailVerificationException ex) {
            return ex.getMessage();
        }
        
        
        return "Your email was verified. Thank you. You may close this page and login.";
    }
    
    // ************************************
    // FORGOT PASSWORD 
    // ************************************
    
    /**
     * Verify if user with this email is allowed to set a new password. 
     * Checks if email exists, has been verified etc. 
     * and eventually generates and emails a new code.
     */
    @PostMapping("/forgot-password/request")
    public ForgotPasswordToSendDTO forgotPasswordGrantAuthorization(
                                        @RequestBody @Validated ForgotPasswordRequestWithEmailSentDTO body, 
                                        BindingResult validation) 
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        String email = body.email();
        
        this.forgotPasswordService.grantAuthorizationCodeToEmail(email);
        
        String message = "We've just sent you an email with a unique authorization link. "
                        +"For your security, the link will expire soon and can only be used once.";
        
        return new ForgotPasswordToSendDTO(message);

    }


    /**
     * The frontend sends a request here, to verify 
     * whether the user (associated with the input code) 
     * can actually set new password on the "set new password" page. 
     * Then we should mark this code as clicked or similar logic.
     */
    @PostMapping("/forgot-password/verify")
    public ForgotPasswordToSendDTO forgotPasswordVerifyAuthorizationBeforeClick(
                                                  @RequestBody @Validated VerifyForgotPasswordCodeSentDTO body,
                                                  BindingResult validation)
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        UUID code;
        
        // verify that code is a valid UUID, but don't tell client
        // that we're doing it
        try {
            
            code = StringHelper.parseUUID(body.code());

        } catch(InvalidUUIDStringException ex) {
            throw new ForgotPasswordVerificationException("Code is not valid (error 10).");
        }
        
        this.forgotPasswordService.verifyAuthorizationCodeWhenClick(code);
        
        return new ForgotPasswordToSendDTO("You are authorized to access the page to set a new password.");
    }


    /**
     *  After the user is done typing their password at frontend, 
     *  frontend sends request with new password. 
     *   Here we must run all checks. If all good, new password is set.
     */
    @PostMapping("/forgot-password/reset")
    public ForgotPasswordToSendDTO setNewPasswordIfAuthorized(
            @RequestBody @Validated ForgotPasswordNewPasswordSentDTO body,
            BindingResult validation)
    {

        PayloadValidationHelper.requireNoErrors(validation);

        UUID code;

        // verify that code is a valid UUID, but don't tell client
        // that we're doing it
        try {

            code = StringHelper.parseUUID(body.code());

        } catch(InvalidUUIDStringException ex) {
            throw new ForgotPasswordVerificationException("Code is not valid (error 10).");
        }
        
        String newPassword = body.newPassword();
        
        //
        this.forgotPasswordService.setNewPasswordIfAuthorized(newPassword, code);
        
        return new ForgotPasswordToSendDTO("You've successfully reset your password. "  
                                            +"You can now login with this new password.");
    }



}