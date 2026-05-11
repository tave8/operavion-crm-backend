package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record NewClientSentDTO(

        @NotNull(message = "Missing 'legalName' field")
        String legalName,

        @NotNull(message = "Missing 'phone' field")
        String phone,

        @NotNull(message = "Missing 'email' field")
        @Email(message = "Email is not valid.")
        String email,

        @NotNull(message = "Missing 'vat' field")
        String vat,

        @NotNull(message = "Missing 'legalAddress' field")
        String legalAddress,

        @NotNull(message = "Missing 'legalAddressLat' field")
        @DecimalMin(value = "-90.0", message = "'legalAddressLat' must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "'legalAddressLat' must be between -90 and 90")
        Double legalAddressLat,

        @NotNull(message = "Missing 'legalAddressLon' field")
        @DecimalMin(value = "-180.0", message = "'legalAddressLon' must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "'legalAddressLon' must be between -180 and 180")
        Double legalAddressLon
        
) {
}
