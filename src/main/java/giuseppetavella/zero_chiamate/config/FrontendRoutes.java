package giuseppetavella.zero_chiamate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>Pre-built frontend routes</h1>
 * Logic: The backend shouldn't care about frontend routes.
 * 
 * The backend shouldn't care whether the actual dashboard frontend route
 * is at /u/dashboard, /dash or /whatever/dash.
 * 
 * Thus, backend should provide the minimal information 
 * and be as generic as possible.
 * 
 * Frontend should take care of translating that information into the actual route,
 * and thus re-routing the user.
 * 
 * The mechanism through which we accomplish this separation of concerns
 * could be the following:
 * We can use query params at the root frontend route.
 * Frontend should leave at least this route non-protected.
 * It can be any route, as long as there's no security mechanism.
 * 
 * This way, the route can be accessed, the code in the route can be executed,
 * and the query params can be processed for re-routing.
 * 
 * For example, the backend could communicate to the frontend,
 * "user has successfully verified email" with the following:
 * <code>/?emailVerificationSuccess=true</code>
 * and failed to verify email with <code>/?emailVerificationSuccess=false</code>.
 * 
 * Frontend then parses the query param, and then re-routes user,
 * shows a toast etc.
 * 
 * Examples of how frontend could translate backend "shortcuts":
 * <pre>
 *     
 *             BACKEND                     |              FRONTEND
 *     -----------------------------------------------------------------------
 *     
 *     /?emailVerificationSuccess=true    ->   show toast, then re-route to /u/dashboard    
 *     
 *     /?emailVerificationSuccess=false   ->   re-route to /auth/invalid-email-verification-code
 *     
 * </pre>
 * 
 * This single, public frontend route,
 * creates a central place of communication for frontend and backend,
 * while leaving backend disinterested in frontend routing logic.
 *
 * By calling the methods of this bean, we take it a step further, 
 * and we don't even care about any of this; We just call a method.
 * 
 */
@Component
public class FrontendRoutes {

    @Autowired
    private AppEnvironment appEnvironment;

    public String dashboard() {
        return buildShortcut(FrontendShortcutRoute.DASHBOARD);
    }

    public String emailVerificationSuccess() {
        return buildShortcut(FrontendShortcutRoute.EMAIL_VERIFICATION_SUCCESS);
    }

    public String emailVerificationInvalid() {
        return buildShortcut(FrontendShortcutRoute.EMAIL_VERIFICATION_INVALID);
    }
    
    public String startOperatorShift(String token) {
        return buildShortcut(FrontendShortcutRoute.START_OPERATOR_SHIFT, token);
    }
    
    // add more pre-built frontend routes here...     

    
    
    /**
     * Generate a URL at the <code>/shortcut</code> endpoint 
     * that points to the frontend 
     * of the current app's environment.
     * 
     * If the server is running in production, the frontend URL
     * will point to production frontend.
     * 
     * If the server is running local, the frontend URL will point 
     * to the local frontend, and so on.
     * 
     * The URL contains specific, predictable query parameters that the frontend
     * will have to interpret, to determine where to re-route the user, 
     * and/or what actions to perform.
     * 
     * @return
     */
    private String buildShortcut(FrontendShortcutRoute route) {
        return appEnvironment.buildFrontendUrl("/shortcut?for=" + route.getValue());
    }

    private String buildShortcut(FrontendShortcutRoute route, String token) {
        return appEnvironment.buildFrontendUrl("/shortcut?for=" + route.getValue() + "&token="+token);
    }
    
}