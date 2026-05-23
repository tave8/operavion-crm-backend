package giuseppetavella.demo_login_system.domain.business.auth;

import giuseppetavella.demo_login_system.domain.entities.companies.CompaniesService;
import giuseppetavella.demo_login_system.domain.entities.users.UsersService;
import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.exceptions.EmailVerificationException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;
import giuseppetavella.demo_login_system.infrastructure.email.EmailService;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.LoginSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.OperatorLoginSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.SignupSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.to_send.AfterLoginDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.to_send.AfterSignupDTO;
import giuseppetavella.demo_login_system.security.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
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
    private EmailService appEmailService;
    
    @Autowired
    private EmailVerificationService emailVerificationService;
    
    @Autowired
    private SeedDataOnSignupService seedDataOnSignupService;

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
        Company companyFromDB = this.companiesService.addCompany(body);
        
        // add the admin and associate it to the company
        User newUserFromDB = this.usersService.addAdminOnlyOnce(body, companyFromDB);

        // send email verification code to the admin
        String verificationUrl = this.emailVerificationService.generateNewEmailVerificationUrl(newUserFromDB);
        
        // send email
        this.appEmailService.sendVerifyEmail(newUserFromDB, verificationUrl);
        
        // seed data - this could be done async
        this.seedDataOnSignupService.seedStandardChecklists(companyFromDB);
        
        return new AfterSignupDTO(
                newUserFromDB, 
                "Signup was successful. We've sent you an email with a link to confirm that it's you."
        );
    }


    
    
}