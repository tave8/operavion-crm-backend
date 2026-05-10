package giuseppetavella.demo_login_system.services.base;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.enums.UserRole;
import giuseppetavella.demo_login_system.exceptions.EmailVerificationException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;
import giuseppetavella.demo_login_system.payloads.in_request.LoginSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.OperatorLoginSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.SignupSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.reset_password.ResetPasswordOldPasswordSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.AfterLoginDTO;
import giuseppetavella.demo_login_system.payloads.in_response.AfterSignupDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ProfileToSendDTO;
import giuseppetavella.demo_login_system.security.TokenTools;
import giuseppetavella.demo_login_system.services.AppEmailService;
import giuseppetavella.demo_login_system.services.CompaniesService;
import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsersService usersService;
    
    @Autowired
    private CompaniesService companiesService;

    @Autowired
    private TokenTools tokenTools;
    
    @Autowired
    private AppEmailService appEmailService;
    
    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private PasswordEncoder bcrypt;


    
    /**
     *  Call this only when the user is trying to login.
     *      * The user can proceed with the login, only if the account 
     *      * associated with the email that they sent, is 
     */
    public AfterLoginDTO login(LoginSentDTO body) throws NotFoundException {
        
        User userFound;
        String accessToken;

        try {

            userFound = this.usersService.findByEmail(body.email());

            // we compare the password coming from the request's body
            // with the actual password found in the database
            boolean isPasswordMatch = this.bcrypt.matches(body.password(), userFound.getPassword());

            // se la password dell'utente corrisponde a quella che si trova
            // nell'utente che ha questa email, vuol dire che l'utente si è loggato
            // con successo, quindi crea il token
            if (isPasswordMatch) {
                
                accessToken = this.tokenTools.generateToken(userFound);
                
            } else {
                throw new UnauthorizedException("Wrong credentials.");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Wrong credentials.");
        }
        
        
        // user has not verified their email
        if(!userFound.isVerifiedEmail()) {
            
            String verificationUrl = this.emailVerificationService.generateNewEmailVerificationUrl(userFound);
            this.appEmailService.sendVerifyEmail(userFound, verificationUrl);
            
            // System.out.println("USER HAS NOT VERIFIED THEIR EMAIL");
            throw new EmailVerificationException("User can login only after verifying their email. "
                                                +"An email has been sent with a new verification link.");
        }
        
        
        return new AfterLoginDTO(
                accessToken,
                userFound,
                "You've logged in successfully."
            );
        
    }


    /**
     *  Login an operator.
     */
    public AfterLoginDTO loginOperator(OperatorLoginSentDTO body) throws NotFoundException 
    {

        User userFound;
        String accessToken;

        try {

            userFound = this.usersService.findByUsername(body.username());

            // we compare the password coming from the request's body
            // with the actual password found in the database
            boolean isPasswordMatch = this.bcrypt.matches(body.password(), userFound.getPassword());

            // se la password dell'utente corrisponde a quella che si trova
            // nell'utente che ha questa email, vuol dire che l'utente si è loggato
            // con successo, quindi crea il token
            if (isPasswordMatch) {

                accessToken = this.tokenTools.generateToken(userFound);

            } else {
                throw new UnauthorizedException("Wrong credentials.");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Wrong credentials.");
        }

        return new AfterLoginDTO(
                accessToken,
                userFound,
                "You've logged in successfully."
        );

    }
    


    /**
     * Sign up a company + admin user.
     */
    @Transactional
    public AfterSignupDTO signup(SignupSentDTO body) 
    {
        
        // add company to DB
        Company company = this.companiesService.addCompany(body);
        
        // add the admin and associate it to the company
        User newUser = this.usersService.addAdminOnlyOnce(body, company);

        // send email verification code to the admin
        String verificationUrl = this.emailVerificationService.generateNewEmailVerificationUrl(newUser);
        
        // send email
        this.appEmailService.sendVerifyEmail(newUser, verificationUrl);
        
        return new AfterSignupDTO(
                newUser, 
                "Signup was successful. We've sent you an email with a link to confirm that it's you."
        );
    }


    
    
}