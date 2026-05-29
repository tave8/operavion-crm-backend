package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailVerificationException;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.exceptions.UnauthorizedException;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.LoginSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.OperatorLoginSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.SignupSentDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.to_send.AfterLoginDTO;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.to_send.AfterSignupDTO;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import giuseppetavella.zero_chiamate.security.TokenTools;
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
    private ForgotPasswordAuthorizationMailer forgotPasswordAuthorizationMailer;
    
    @Autowired
    private VerifyEmailMailer verifyEmailMailer;
    
    @Autowired
    private AuthEmailVerificationService authEmailVerificationService;
    
    @Autowired
    private SeedDataOnSignupService seedDataOnSignupService;

    @Autowired
    private PasswordEncoder bcrypt;
    
    @Autowired
    private StripeAPIService stripeAPIService;


    
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
                
                accessToken = this.tokenTools.generateToken(userFound.getId().toString());
                
            } else {
                throw new UnauthorizedException("Wrong credentials.");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Wrong credentials.");
        }
        
        
        // user has not verified their email
        if(!userFound.isVerifiedEmail()) {
            
            String verificationUrl = this.authEmailVerificationService.generateNewEmailVerificationUrl(userFound);
            
            verifyEmailMailer.send(userFound, verificationUrl);
            
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

                accessToken = this.tokenTools.generateToken(userFound.getId().toString());

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

        // ************************
        // CREATE NEW COMPANY IN MY DB
        // ************************
        
        // add company to DB
        Company companyFromDB = this.companiesService.addCompany(body);

        // ************************
        // CREATE NEW ADMIN & ASSOCIATE IT TO COMPANY
        // ************************
        
        // add the admin and associate it to the company
        User newUserFromDB = this.usersService.addAdminOnlyOnce(body, companyFromDB);

        // ************************
        // CREATE NEW COMPANY IN STRIPE API
        // ************************

        // create Stripe customer and save ID to company
        String stripeCustomerId = this.stripeAPIService.createCustomer(
                companyFromDB.getEmail(),
                companyFromDB.getLegalName()
        );

        // we associate the Stripe customer ID to this 
        // newly created company
        companyFromDB.setStripeCustomerId(stripeCustomerId);

        // ************************
        // GENERATE EMAIL VERIFICATION CODE & SEND EMAIL
        // ************************

        // send email verification code to the admin
        String verificationUrl = this.authEmailVerificationService.generateNewEmailVerificationUrl(newUserFromDB);

        // send email
        verifyEmailMailer.send(newUserFromDB, verificationUrl);
        
        // ************************
        // SEED COMPANY DATA
        // ************************
        
        // seed data
        this.seedDataOnSignupService.seedStandardChecklists(companyFromDB);
        
        return new AfterSignupDTO(
                newUserFromDB, 
                "Signup was successful. We've sent you an email with a link to confirm that it's you."
        );
    }


    
    
}