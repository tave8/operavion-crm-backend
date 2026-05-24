package giuseppetavella.zero_chiamate.api.controllers.auth;

import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.reset_password.ResetPasswordOldPasswordSentDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class ResetPasswordController {

    @Autowired
    private UsersService usersService;


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
    
}
