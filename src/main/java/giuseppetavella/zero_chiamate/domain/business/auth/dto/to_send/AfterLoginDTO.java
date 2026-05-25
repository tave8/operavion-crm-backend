package giuseppetavella.zero_chiamate.domain.business.auth.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.companies.dto.to_send.CompanyToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.ProfileToSendDTO;

public class AfterLoginDTO {
    
    private final String accessToken;
    private final String message;
    private final boolean mustChangePasswordNow;
    private final ProfileToSendDTO user;
    
    public AfterLoginDTO(String accessToken, User user, String message) 
    {
        this.accessToken = accessToken;
        this.message = message;
        this.mustChangePasswordNow = user.mustChangePasswordNow();
        // TODO: refactor 
        this.user = new ProfileToSendDTO(
                user, 
                new CompanyToSendDTO(user.getCompany())
        );
    }
    
    public AfterLoginDTO(String accessToken, User user) {
        this(accessToken, user, "");
    }
    

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isMustChangePasswordNow() {
        return mustChangePasswordNow;
    }

    public String getMessage() {
        return message;
    }

    public ProfileToSendDTO getUser() {
        return user;
    }
}
