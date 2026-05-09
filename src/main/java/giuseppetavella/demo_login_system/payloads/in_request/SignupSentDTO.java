package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupSentDTO(
        
        @NotBlank(message = "Missing 'email' field.")
        @Email(message = "Email must be valid.")
        String email,

        @NotBlank(message = "Missing 'password' field.")
        @Size(min = 6, max = 20, message = "Password must have between 6 and 20 characters.")
        String password,

        @NotBlank(message = "Missing 'firstname' field.")
        @Size(min = 2, max = 30, message = "Firstname must have between 2 and 30 characters.")
        String firstname,

        @NotBlank(message = "Missing 'lastname' field.")
        @Size(min = 2, max = 30, message = "Lastname must have between 2 and 30 characters.")
        String lastname,

        @NotBlank(message = "Missing 'legalName' field.")
        @Size(min = 2, max = 30, message = "Legal name must have between 2 and 30 characters.")
        String legalName
) 
{
}
