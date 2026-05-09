package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.enums.UserRole;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.EnumHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewUserSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.UpdatedProfileSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.NewUserToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ProfileToSendDTO;
import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UsersController {
    
    @Autowired
    private UsersService usersService;


    /**
     * Get my profile.
     */
    @GetMapping("/me")
    public ProfileToSendDTO getOwnProfile(@AuthenticationPrincipal User currentUser)
    {
        return new ProfileToSendDTO(
                this.usersService.findById(currentUser.getId())
        );
    }
    
    /**
     * Update my profile.
     */
    @PutMapping("/me")
    public ProfileToSendDTO updateOwnProfile(@AuthenticationPrincipal User currentUser,
                                             @RequestBody @Validated UpdatedProfileSentDTO body)
    {
        return new ProfileToSendDTO(
                this.usersService.updateOwnProfile(currentUser, body)
        );
    }

    
    /**
     * Add a user - only admin is authorized.
     */
    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public NewUserToSendDTO addUser(@AuthenticationPrincipal User currentUser,
                                     @RequestBody @Validated NewUserSentDTO body)
    {
        // parse a string into actual enum constant, if you can 
        UserRole desiredRole = EnumHelper.parseEnum(UserRole.class, body.role());
        
        // admin cannot add another admin
        AuthorizationHelper.requireAdminAddValidRole(currentUser, desiredRole);
        
        // if desired role is coordinator, the email must be valid
        // whereas for operator role, we don't use the email
        if(desiredRole.equals(UserRole.COORDINATOR)) {
            StringHelper.requireValidEmailElseThrowWith(
                    body.email(), 
                    "When adding a coordinator, email must exist and must be valid."
            );
        }
        
        // to add a user, we need the company
        // to get the company, we get it from the currentUser, which is the admin
        Company company = currentUser.getCompany();
        
        String tempPassword = StringHelper.generatePassword();
        
        User newUser = this.usersService.addUserBasedOnRole(body, desiredRole, company, tempPassword);
        
        return new NewUserToSendDTO(newUser, tempPassword);
    }



    /**
     * Upload my new avatar image.
     */
    @PostMapping("/me/avatar-image")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileToSendDTO uploadMyAvatarImage(@AuthenticationPrincipal User currentUser,
                                                @RequestParam("avatar_image") MultipartFile avatarImage)
    {
        return new ProfileToSendDTO(
                this.usersService.uploadMyAvatarImage(currentUser, avatarImage)
        );
    }
    
}
