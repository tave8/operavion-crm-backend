package giuseppetavella.zero_chiamate.api.controllers.auth;

import giuseppetavella.zero_chiamate.domain.business.auth.ForgotPasswordService;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.forgot_password.ForgotPasswordNewPasswordSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.forgot_password.ForgotPasswordRequestWithEmailSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.forgot_password.VerifyForgotPasswordCodeSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.to_send.forgot_password.ForgotPasswordToSendDTO;
import giuseppetavella.zero_chiamate.exceptions.ForgotPasswordVerificationException;
import giuseppetavella.zero_chiamate.exceptions.InvalidUUIDStringException;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.helpers.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth/forgot-password")
public class ForgotPasswordController {


    @Autowired
    private ForgotPasswordService forgotPasswordService;

    /**
     * Verify if user with this email is allowed to set a new password. 
     * Checks if email exists, has been verified etc. 
     * and eventually generates and emails a new code.
     */
    @PostMapping("/request")
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
    @PostMapping("/verify")
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
    @PostMapping("/reset")
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
