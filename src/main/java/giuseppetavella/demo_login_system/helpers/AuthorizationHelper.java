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

    /**
     * Require that a user exists and that their ID is set,
     * which is the assumption behind "this user is in DB".
     */
    public static void requireUser(User user) throws UnauthorizedException
    {
        
        // user is null
        if(user == null) {
            throw new UnauthorizedException("User is null. Are you sure it was passed correctly "
                                            +"and that a user was expected?");
        }
        
        // user is not null, but its ID is null
        if(user.getId() == null) {
            throw new UnauthorizedException("User is not null but does not seem to exist "
                                            +"in database either (assumption).");
        }
        
    }
    
}
