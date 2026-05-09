package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.payloads.in_request.UpdatedProfileSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ProfileToSendDTO;
import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
