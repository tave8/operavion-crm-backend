package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.enums.UserRole;
import giuseppetavella.demo_login_system.exceptions.*;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewUserSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.SignupSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.UpdatedProfileSentDTO;
import giuseppetavella.demo_login_system.repositories.UsersRepository;
import giuseppetavella.demo_login_system.services.base.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UsersService {
    
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private AppEmailService appEmailService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * Find a user by ID.
     */
    public User findById(UUID userId) throws NotFoundException {
        return usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId, "user"));
    }

    public User findById(String userId) throws NotFoundException {
        try {
            return this.findById(UUID.fromString(userId));
        } catch(IllegalArgumentException ex) {
            throw new InvalidUUIDStringException(userId);
        }
    }
    
    // public User findAllWhoSignedupOnDay() {
    //    
    // }

    /**
     * Find a user by email.
     */
    public User findByEmail(String email) throws NotFoundException {
        if(email == null) {
            throw new NotFoundException("An email that was null was given.");
        }
        
        User userFound = this.usersRepository.findByEmail(email);
        
        if (userFound == null) {
            throw new NotFoundException("User with email '" + email + "' was not found.");
        }
        
        return userFound;
    }


    /**
     * Find a user by username.
     */
    public User findByUsername(String email) throws NotFoundException {
        if(email == null) {
            throw new NotFoundException("A username that was null was given.");
        }

        User userFound = this.usersRepository.findByUsername(email);

        if (userFound == null) {
            throw new NotFoundException("User with username '" + email + "' was not found.");
        }

        return userFound;
    }

    /**
     * A user with the given email exists?
     */
    public boolean existsByEmail(String email) {
        return this.usersRepository.existsByEmail(email);
    }

    
    
    /**
     * A user with this ID exists?
     */
    public boolean existsById(UUID userId) {
        if(userId == null) {
            return false;
        }
        return this.usersRepository.existsById(userId);
    }
    
    /**
     * Add a user.
     * Checks if the email does not exist.
     */
    private User addUser(User user) throws UnauthorizedException  {
        if(this.existsByEmail(user.getEmail())) {
            throw new UnauthorizedException("This email already exists.");
        }
        return this.usersRepository.save(user);
    }

    /**
     * 
     * Add a user with this role to this company.
     * 
     * @param body
     * @param role
     * @param company
     * @return
     * @throws UnauthorizedException
     */
    private User addUser(SignupSentDTO body, UserRole role, Company company) throws UnauthorizedException {
        
        String uniqueUsername = this.generateUniqueUsernameFrom(body.firstname(), body.lastname());
        
        String hashedPassword = this.bcrypt.encode(body.password());
        
        User newUser = new User(
                company,
                body.email(),
                hashedPassword,
                body.firstname(),
                body.lastname(),
                role,
                uniqueUsername
        );
        
        return this.addUser(newUser);
    }

    
    /**
     * Add admin.
     */
    public User addAdmin(SignupSentDTO body, Company company) {
        // TODO: check that no other admin exists for this company
        
        return this.addUser(body, UserRole.ADMIN, company);
    }


    /**
     * Add user to company based on role.
     */
    public User addUserBasedOnRole(NewUserSentDTO body, 
                                   UserRole role, 
                                   Company company, 
                                   String tempPassword) 
    {
        String hashedTempPassword = this.bcrypt.encode(tempPassword);

        String uniqueUsername = this.generateUniqueUsernameFrom(body.firstname(), body.lastname());
        
        // if role is coordinator:
        if(role.equals(UserRole.COORDINATOR)) {
            
            User newUser = new User(
                    company,
                    body.email(),
                    hashedTempPassword,
                    body.firstname(),
                    body.lastname(),
                    role,
                    uniqueUsername
            );
            
            // send email with verify email
            this.appEmailService.sendVerifyEmailWithVerificationUrl(newUser);
            
            return this.addUser(newUser);
            
        }
        
        // if role is operator
        if(role.equals(UserRole.OPERATOR)) {

            User newUser = new User(
                    company,
                    null,
                    hashedTempPassword,
                    body.firstname(),
                    body.lastname(),
                    role,
                    uniqueUsername
            );
            
            return this.addUser(newUser);    
                
        }
        
        // if role is operator:
        // - generate temporary password     
        
        throw new UnauthorizedException("No logic was defined to add user role '"+role+"' based on role.");
        
    }


    /**
     * Update my profile, given the ID.
     */

    public User updateOwnProfile(User profile, 
                                 UpdatedProfileSentDTO profileBody)
    {
        profile.setFirstname(profileBody.firstname());
        profile.setLastname(profileBody.lastname());
        return this.usersRepository.save(profile);
    }

    
    
    /**
     * Upload my new avatar image.
     */
    public User uploadMyAvatarImage(User user, MultipartFile avatarImage) throws InvalidFileUploadedException, 
                                                                                 FileUploadException
    {
        
        // get URL of uploaded image
        String avatarUrl = this.imageUploadService.uploadAvatarImage(avatarImage);

        // update author
        user.setAvatarUrl(avatarUrl);

        // save user      
        return this.usersRepository.save(user);

    }


    /**
     * Generate a unique username.
     * 
     * @param firstname
     * @param lastname
     * @return
     */
    private String generateUniqueUsernameFrom(String firstname, String lastname) {

        final int NUMBER_LENGTH = 4;

        // base = "mario.rossi"
        String base = firstname.toLowerCase().trim() + "." + lastname.toLowerCase().trim();

        String username;

        do {
            // Generate a random number with NUMBER_LENGTH digits
            // e.g. Math.pow(10, 6) = 1000000
            // Math.random() * 1000000 = 324234.123
            // (int) 324234.123 = 324234
            int randomNumber = (int) (Math.random() * Math.pow(10, NUMBER_LENGTH));

            // Concatenate base + number
            // e.g. "mario.rossi" + 324234 = "mario.rossi324234"
            username = base + randomNumber;

        } while (this.usersRepository.existsByUsername(username));
        // Keep looping if the username already exists in the DB
        // Exit when a unique one is found

        return username;
    }
    
}
