package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.exceptions.InvalidDataFormatException;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;

public class AuthorizationHelper {

    /**
     * Require that two users are the same, by comparing their UUID.
     * 
     * @param user1
     * @param user2
     */
    public static void requireSameUser(User user1, User user2) throws UnauthorizedException, 
                                                                        InvalidDataFormatException
    {
        // if not same user
        if(!User.isSameUser(user1, user2)) {
            throw new UnauthorizedException("You are not authorized to access this resource.");
        }
        
    }
    
}
