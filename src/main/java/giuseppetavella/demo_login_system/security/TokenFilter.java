package giuseppetavella.demo_login_system.security;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;
import giuseppetavella.demo_login_system.payloads.in_response.ErrorsToSendDTO;
import giuseppetavella.demo_login_system.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;


@Component
@Order(1)
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenTools tokenTools;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ObjectMapper objectMapper;



    /**
     * Send an error response with custom status code.
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");

        ErrorsToSendDTO error = new ErrorsToSendDTO(message);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
    
    
    /**
     * Send an Unauthorized error response.
     */
    private void sendUnauthorizedErrorResponse(HttpServletResponse response, String message) throws IOException {
        this.sendErrorResponse(response, message, 401);
    }

    /**
     * Send a Forbidden error response.
     */
    private void sendForbiddenErrorResponse(HttpServletResponse response, String message) throws IOException {
        this.sendErrorResponse(response, message, 403);
    }


    /**
     * NOTE: 
     * You should take care of standardizing the exceptions thrown at this level
     * (Security Filter Chain) with those thrown at the Controller level.
     *      
     * here we are in the Security Filter Chain layer, and 
     * we do not have access to the Spring exception catching abstraction.
     * Therefore we need to must build our error responses manually,
     * ideally using the same JSON class, and therefore the same JSON response,
     * that we use inside the exceptions that are thrown in the controllers.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, UnauthorizedException
    {

        AntPathMatcher matcher = new AntPathMatcher();
        String path = request.getServletPath();
        
        boolean isRoot = matcher.match("/", path);
        boolean isFavicon = matcher.match("/favicon.ico", path);
        
        boolean isAuthPath = matcher.match("/auth/**", path);
        boolean isLoginPath = matcher.match("/auth/login", path);
        boolean isFileUpload = matcher.match("/file-upload/**", path);
        // boolean isNotificationsPath = matcher.match("/notifications/**", path);
        // boolean isAI = matcher.match("/ai/**", path);
        // boolean isPdfGeneration =  matcher.match("/pdf-generation/**", path);
        // boolean isCsvGeneration =  matcher.match("/csv-generation/**", path);
        
        // if(isNotificationsPath) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }
        //
        if(isFileUpload) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if(isFavicon) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // if(isAI || isPdfGeneration || isCsvGeneration) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }
        
        // there are no controls on root endpoint
        if(isRoot) {
            // System.out.println("this is the / path");
            filterChain.doFilter(request, response);
            return;
        }


        // *************************
        // IF THE USER IS TRYING TO LOGIN
        // *************************
        
        if(isLoginPath) {
            // System.out.println("TRYING TO LOGIN");
            filterChain.doFilter(request, response);
            return;
        }
        
        // *************************
        // THIS IS AN AUTHENTICATION ENDPOINT
        // *************************
        
        if(isAuthPath) {
            // System.out.println("AUTHENTICATION ENDPOINT THAT IS NOT LOGIN");
            // System.out.println("this is an /auth/** path");
            filterChain.doFilter(request, response);
            return;
        }
        
        // System.out.println("PATH PROTECTED BY LOGIN");
        
        // verify that header contains access token
        String authHeader = request.getHeader("Authorization");
        
        boolean authHeaderNotExists = authHeader == null;


        // *************************
        // AUTHORIZATION HEADER DOES NOT EXIST
        // *************************
        
        
        // authorization header does not exist
        if(authHeaderNotExists) {
            
            this.sendUnauthorizedErrorResponse(response,"Missing Authorization header. The requested resource was defined "
                                                                 +"to require user authentication expressed "
                                                                 +"through the Authorization header in the request, however "
                                                                 +"no such header was found. Therefore this resource cannot be accessed."
                                                                  +"Either this resource should not be protected, "
                                                                    +"or the Authorization header is missing.");
            return;
        }
        
        
        boolean tokenNotExists = !authHeader.startsWith("Bearer ");


        // *************************
        // ACCESS TOKEN DOES NOT EXIST
        // *************************
        
        // access token does not exist
        if(tokenNotExists) {
            this.sendUnauthorizedErrorResponse(response,"Missing access token in Authorization header. "
                                                                +"The requested resource was defined to require user authentication expressed "
                                                                    +"through the Authorization header in the request, however "
                                                                    +"the access token was not found in such header. Therefore this resource "
                                                                    +"cannot be accessed."
                                                                    +"Either this resource should not be protected, or the access token is missing."
                                                                    +"Make sure that the access token is in Authorization header with "
                                                                    +"exactly this format 'Bearer mytoken'");
            return;
        }
        

        // we get the token
        String accessToken = authHeader.replace("Bearer ", "");


        // *************************
        // ACCESS TOKEN IS NOT VALID/IS EXPIRED
        // *************************
        
        // if token verification fails
        try {
            // verify that token is valid etc.
            tokenTools.verifyToken(accessToken);

        } catch(UnauthorizedException ex) {
            this.sendUnauthorizedErrorResponse(response,"Access token is expired, malformed or has been tampered with. "
                                                                 +"The user should try to do a new login, and see if that solves the problem.");
            return;
        }


        UUID userId;

        try {
            // 1. extract user's ID from token
            userId = this.tokenTools.extractIdFromToken(accessToken);

        } catch(RuntimeException ex) {
            this.sendUnauthorizedErrorResponse(response, "Access token is not valid (error 3).");
            return;
        }

        User currentUser;

        // *************************
        // USER ASSOCIATED WITH TOKEN DOES NOT EXIST
        // *************************

        try {
            currentUser = this.usersService.findById(userId);
        } catch (NotFoundException ex) {
            this.sendUnauthorizedErrorResponse(response,"Access token was valid but "
                                                                +"the user associated to it no longer exists. User ID was: '"
                                                                + userId + "'. Maybe the user was deleted?");
            return;
        }


        // *************************
        // USER MUST VERIFY THEIR EMAIL AND HAS NOT
        // *************************
        
        // if user has not verified their email, only 
        // if their email is required to be verified
        if(!currentUser.isVerifiedEmailIfRequired()) {
            this.sendForbiddenErrorResponse(response, "The user with ID '" 
                                                            + currentUser.getId() 
                                                            + "' has not verified their email.");
            return;
        }

        
        // *************************
        // USER MUST CHANGE THEIR PASSWORD AND HAS NOT
        // *************************
        // if(!currentUser.isVerifiedEmailIfRequired()) {
        //     this.sendForbiddenErrorResponse(response, "The user with ID '"
        //             + currentUser.getId()
        //             + "' has not verified their email.");
        //     return;
        // }




        // 3. now we need to make this user available to the Security Context
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        // we now set the current user of this request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // System.out.println("do filter internal called!");

        
        filterChain.doFilter(request, response);

    }
    
    // we can specify which paths not to filter,
    // for example the paths starting with /auth   

    /**
     * Disable this security filter for endpoints that:
     * - start with /auth
     * - is / (root)
     * 
     * 
     * In this method we specify in which cases
     * our security filter should not be called.
     * For example, we might want to disable this security filter
     * for all endpoints starting with /auth
     */
    // protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    //     AntPathMatcher matcher = new AntPathMatcher();
    //     String path = request.getServletPath();
    //
    //     // the /auth/** endpoint is excluded from the filter
    //     // boolean isAuthPath = matcher.match("/auth/**", path);
    //     // the / endpoint is excluded from the filter
    //     boolean isRoot = matcher.match("/", path);
    //    
    //     return isRoot;
    // }
    


}