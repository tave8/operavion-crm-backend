package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.enums.UserRole;
import giuseppetavella.demo_login_system.exceptions.*;
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
    public User addUser(User user) throws UnauthorizedException  {
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
    public User addUser(SignupSentDTO body, UserRole role, Company company) throws UnauthorizedException {
        String hashedPassword = this.bcrypt.encode(body.password());
        
        User newUser = new User(
                company,
                body.email(),
                hashedPassword,
                body.firstname(),
                body.lastname(),
                role
        );
        
        return this.addUser(newUser);
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
    
}
