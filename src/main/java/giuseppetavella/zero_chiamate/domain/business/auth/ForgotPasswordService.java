package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailVerificationException;
import giuseppetavella.zero_chiamate.exceptions.ForgotPasswordVerificationException;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.helpers.TimeHelper;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {
    
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private AuthEmailService authEmailService;
    
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private PasswordEncoder bcrypt;
    
    // the base endpoint at which the user 
    // will land upon clicking the authorization link
    private final String FRONTEND_ENDPOINT = "/auth/forgot-password/verify";
    
    // this is a constructor-injected dependency
    private final String frontendUrl;
    
    // how many minutes the user must wait before being authorized
    // to receive a new code.
    private final long NEXT_CODE_WAIT_TIME;
    
    // how many minutes the user has to set a new password,
    // starting when the code is generated.
    // (TTL = time to live) 
    private final long SET_PASSWORD_TTL;

    // we inject dependencies
    public ForgotPasswordService(
            @Qualifier("frontendUrl") String frontendUrl,
            @Qualifier("forgotPasswordNextCodeWaitTime") long forgotPasswordNextCodeWaitTime,
            @Qualifier("forgotPasswordSetPasswordTTL") long forgotPasswordSetPasswordTTL)
    {
        this.frontendUrl = frontendUrl;
        this.NEXT_CODE_WAIT_TIME = forgotPasswordNextCodeWaitTime;
        this.SET_PASSWORD_TTL = forgotPasswordSetPasswordTTL;
    }



    /**
     * ## FORGOT PASSWORD: STEP 1 (GRANT AUTHORIZATION CODE TO EMAIL)
     * 
     * Grant the user associated with the input email,
     * the permission to set a new password.
     * Includes sending an email with verification link.
     *
     * @throws ForgotPasswordVerificationException if the user is not authorized to set a password
     */
    @Transactional
    public void grantAuthorizationCodeToEmail(String email) throws ForgotPasswordVerificationException 
    {
        try {
            // this email must exist and must have been verified
            User emailOwner = this.requireVerifiedEmail(email);
            
            // if this email has requested a forgot password in the last x time     
            this.requireLimitedAttempts(emailOwner);
            
            // // if all verification steps are completed, generate a new code 
            ForgotPasswordCode newCode = this.whenGenerateNewCode(emailOwner);
            //
            //
            String authorizationUrl = this.buildForgotPasswordAuthorizationUrl(newCode.getCode());
            //
            // // send an email to the user, with this code     
            authEmailService.sendForgotPasswordAuthorization(emailOwner, authorizationUrl);
            
        } catch(NotFoundException ex) {
            
            // user with this email was not found     
            throw new ForgotPasswordVerificationException("You cannot reset your password. (error 1)");
            
        } catch(EmailVerificationException ex) {
            
            // email was not verified      
            throw new ForgotPasswordVerificationException("You must verify your email first. (error 2)");
            
        } catch(ForgotPasswordVerificationException ex) {
            
            // user has tried too many attempts
            throw new ForgotPasswordVerificationException("You cannot reset your password right now. Try again later. (error 3)");
            
        }
        
    }
    

    /**
     * ## FORGOT PASSWORD: STEP 2 (VERIFY AUTHORIZATION CODE WHEN CLICK)
     * 
     * Triggered when email clicks code link.
     * 
     * Grant the user associated with the input code,
     * the permission to access the page where the user
     * will set the new password. Calling this method 
     * will mark the given code as clicked. 
     */
    @Transactional
    public void verifyAuthorizationCodeWhenClick(UUID codeId) 
    {

        try {
            
            // the code must exist
            ForgotPasswordCode code = this.findById(codeId);
            
            // the code must be non-expired
            this.requireNotExpiredCode(code, SET_PASSWORD_TTL);
            
            // the code must be usable 
            this.requireUsableCode(code);
            
            // the code must be not clicked
            this.requireNotClickedCode(code);
            
            // the code must belong to a user that exists
            User owner = this.usersService.findById(code.getUser().getId());
            
            // the code must belong to a user with verified email
            this.requireVerifiedEmail(owner);
            
            // ALL CONTROLS PASSED ********
            
            // mark code as clicked
            this.forgotPasswordRepository.markCodeAsClicked(code);
            
            // mark all other codes of this owner as unusable,
            // except this code
            this.forgotPasswordRepository.markAllCodesAsUnusableExcept(owner, code);

        } catch(NotFoundException ex) {

            // user with this email was not found     
            throw new ForgotPasswordVerificationException("You are not authorized to reset your email. (error 1)");

        } catch(EmailVerificationException ex) {

            // email was not verified      
            throw new ForgotPasswordVerificationException("You are not authorized to reset your email. (error 2)");

        } catch(ForgotPasswordVerificationException ex) {

            // user has tried too many attempts
            throw new ForgotPasswordVerificationException("You are not authorized to reset your email. (error 3)");

        }
        
    }
    
    

    /**
     * ## FORGOT PASSWORD: STEP 3 (SET NEW PASSWORD IF AUTHORIZED)
     * 
     * Grant the user to finally set the new password, 
     * if the code is still valid.
     */
    @Transactional
    public void setNewPasswordIfAuthorized(String newPassword, UUID codeId) {

        try {

            // the code must exist
            ForgotPasswordCode code = this.findById(codeId);

            // the code must be non-expired
            this.requireNotExpiredCode(code, SET_PASSWORD_TTL);

            // the code must be usable 
            this.requireUsableCode(code);

            // the code must be clicked
            this.requireClickedCode(code);

            // the code must belong to a user that exists
            User owner = this.usersService.findById(code.getUser().getId());

            // the code must belong to a user with verified email
            this.requireVerifiedEmail(owner);

            // ALL CONTROLS PASSED ********

            // set all codes of this user as not usable
            this.forgotPasswordRepository.markAllCodesAsUnusable(owner);
            
            // mark this code as used
            this.forgotPasswordRepository.markCodeAsUsed(code);

            String hashedPassword = this.bcrypt.encode(newPassword);
            
            // set new password
            this.usersRepository.setNewPassword(owner, hashedPassword);

        } catch(NotFoundException ex) {

            // user with this email was not found     
            throw new ForgotPasswordVerificationException("You are not authorized to reset your email. (error 1)");

        } catch(EmailVerificationException ex) {

            // email was not verified      
            throw new ForgotPasswordVerificationException("You are not authorized to reset your email. (error 2)");

        } catch(ForgotPasswordVerificationException ex) {

            // user has tried too many attempts
            throw new ForgotPasswordVerificationException("You are not authorized to reset your email. (error 3)");

        }
        
        


    }

    /**
     * Generate a new code and save it in DB.
     * It also involves making all other codes of this user as unusable.
     *
     * Ideally, this code is what you email to the user, 
     * so that they can click it.
     */
    @Transactional
    private ForgotPasswordCode whenGenerateNewCode(User user)
    {

        // mark all the codes for this user as unusable 
        this.forgotPasswordRepository.markAllCodesAsUnusable(user);

        // this code is the only one, at this moment, that is usable
        ForgotPasswordCode newCode = new ForgotPasswordCode(user);

        return this.forgotPasswordRepository.save(newCode);
    }
    
    /**
     * Get the last code of the given user, if it exists.
     */
    private Optional<ForgotPasswordCode> getLastCodeByUser(User user) 
    {
        return this.forgotPasswordRepository.getLastCodeOfUser(user);
    }

    
    /**
     * Require the given user to not have tried 
     * too many times, i.e. to not have done too many attempts.
     */
    private void requireLimitedAttempts(User user) throws ForgotPasswordVerificationException  
    {
        
        Optional<ForgotPasswordCode> maybeCode = this.getLastCodeByUser(user);
        
        // if the user has no codes, it means they've never requested
        // a forgot password, which means, they've passed the verification
        if(maybeCode.isEmpty()) {
            return;
        }
        
        // the user has at least one forgot password code, 
        // and now we have their last one chronologically
        ForgotPasswordCode code = maybeCode.get();
        
        // since the time the code was generated, 
        // at least these minutes have passed,
        // which means, from the last code, the user has waited
        // at least these minutes
        if(TimeHelper.sinceHavePassedAtLeast(code.getCreatedAt(), NEXT_CODE_WAIT_TIME)) {
            return;
        }
        
        throw new ForgotPasswordVerificationException("The user has tried too many times "
                                                        +"to reset their password. They must wait.");    
    }

    
    /**
     * 
     * Require that this email exists and the user 
     * associated to it has verified it
     * 
     * @throws NotFoundException is a user with this email does not exist
     * @throws EmailVerificationException if this email has not been verified
     */
    private User requireVerifiedEmail(String email) throws NotFoundException, 
                                                            EmailVerificationException
    {

        User emailOwner = this.usersService.findByEmail(email);

        return this.requireVerifiedEmail(emailOwner);
        
    }

    
    /**
     * Require that this owner has a verified email.
     * 
     * 
     */
    private User requireVerifiedEmail(User emailOwner) throws NotFoundException, 
                                                                EmailVerificationException 
    {
        
        // if this email exists but the user has not verified it
        if(!emailOwner.isVerifiedEmail()) {
            throw new EmailVerificationException("Email is not verified.");
        }

        return emailOwner;
        
    }


    /**
     * Require that the code is not expired.
     * 
     * @throws ForgotPasswordVerificationException if code is expired
     */
    private void requireNotExpiredCode(ForgotPasswordCode code, long minutes) throws ForgotPasswordVerificationException
    {
        
        if(TimeHelper.isValidWithin(code.getCreatedAt(), minutes)) {
            return;
        }

        throw new ForgotPasswordVerificationException("The code with ID '" + code.getCode() + "' is expired.");
        
    }
    

    /**
     * Require that the code is usable.
     */
    private void requireUsableCode(ForgotPasswordCode code) throws ForgotPasswordVerificationException
    {
        
        if(code.isUsable()) {
            return;
        }
        
        throw new ForgotPasswordVerificationException("The code with ID '" + code.getCode() + "' is not usable.");
        
    }


    /**
     * Require that the code is clicked.
     */
    private void requireClickedCode(ForgotPasswordCode code) throws ForgotPasswordVerificationException
    {

        if(code.isClicked()) {
            return;
        }

        throw new ForgotPasswordVerificationException("The code with ID '" + code.getCode() + "' is not clicked.");

    }
    
    
    /**
     * Require that the code is not clicked.
     */
    private void requireNotClickedCode(ForgotPasswordCode code) throws ForgotPasswordVerificationException 
    {
    
        if(!code.isClicked()) {
            return;
        }

        throw new ForgotPasswordVerificationException("The code with ID '" + code.getCode() + "' is already clicked.");
        
    }
    

    /**
     * 
     * @return the code
     * @throws NotFoundException if code with the given ID has not been found
     */
    public ForgotPasswordCode findById(UUID codeId) throws NotFoundException 
    {
        return this.forgotPasswordRepository
                    .findById(codeId)
                    .orElseThrow(() -> new NotFoundException("Code with ID '" + codeId + "' has not been found."));
    }
    


    /**
     * Build the forgot password authorization url from the verification code.
     * This will be the clickable link shown in the email.
     */
    private String buildForgotPasswordAuthorizationUrl(String code) {
        // something like:
        // /auth/forgot-password/verify/:code
        String path = FRONTEND_ENDPOINT + "/" + code;
        return this.frontendUrl + path;
    }

    private String buildForgotPasswordAuthorizationUrl(UUID code) {
        return this.buildForgotPasswordAuthorizationUrl(code.toString());
    }
    
}
