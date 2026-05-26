package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.UserRole;
import giuseppetavella.zero_chiamate.helpers.*;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.sent.NewUserSentDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.sent.UpdatedProfileSentDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.NewUserToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
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
        return usersService.toProfileDTO(
                this.usersService.findById(currentUser.getId())
        );
    }
    
    /**
     * Update my profile.
     */
    @PutMapping("/me")
    public ProfileToSendDTO updateOwnProfile(@AuthenticationPrincipal User currentUser,
                                             @RequestBody @Validated UpdatedProfileSentDTO body,
                                             BindingResult validation)
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        return usersService.toProfileDTO(
                this.usersService.updateOwnProfile(currentUser, body)
        );
    }


    /**
     * Get my users, aka my team.
     * It means, all users of my company, except admin.
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<ProfileToSendDTO> getMyUsers(@AuthenticationPrincipal User currentUser
                                             // @RequestParam(value = "role", required = false) String notificationType,
                                             // @RequestParam(value = "page", defaultValue = "0") int page,
                                             // @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                             // @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                             // @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder
    ) 
    {
        // the current logged in user (the admin)
        // has the company
        // so we get users of its company
        
        Company company = currentUser.getCompany();
    
        Page<User> usersPage = this.usersService.getNonAdminUsersByCompany(company);
        
        return usersPage.map(usersService::toProfileDTO);
        
    }
        
    
    /**
     * Add a non-admin user - only admin is authorized.
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserToSendDTO addNonAdminUser(@AuthenticationPrincipal User currentUser,
                                            @RequestBody @Validated NewUserSentDTO body,
                                            BindingResult validation)
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        // parse a string into actual enum constant, if you can 
        UserRole desiredRole = EnumHelper.parseEnum(UserRole.class, body.role());
        
        // admin cannot add another admin
        AuthorizationHelper.requireAdminAddValidRole(currentUser, desiredRole);
        
        // if desired role is coordinator, the email must be valid
        // whereas for operator role, we don't use the email
        if(desiredRole.equals(UserRole.COORDINATOR)) {

            ValidationHelper.requireValidEmailElseThrowWith(
                    body.email(),
                    "When adding a coordinator, email must exist and must be valid."
            );
            
        }
        
        // to add a user, we need the company
        // to get the company, we get it from the currentUser, which is the admin
        Company company = currentUser.getCompany();
        
        String tempPassword = StringHelper.generatePassword();
        
        User newUser = this.usersService.addNonAdminUserBasedOnRole(body, desiredRole, company, tempPassword);
        
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
        return usersService.toProfileDTO(
                this.usersService.uploadMyAvatarImage(currentUser, avatarImage)
        );
    }
    
}
