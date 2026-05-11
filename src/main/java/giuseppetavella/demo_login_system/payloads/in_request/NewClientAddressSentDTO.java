package giuseppetavella.demo_login_system.payloads.in_request;

import jakarta.validation.constraints.NotNull;

public record NewClientAddressSentDTO(

        @NotNull(message = "Missing 'clientId' field")
        String clientId,
        
        @NotNull(message = "Missing 'addressName' field")
        String addressName,
        
        @NotNull(message = "Missing 'address' field")
        String address,

        @NotNull(message = "Missing 'addressLat' field")
        Double addressLat,

        @NotNull(message = "Missing 'addressLon' field")
        Double addressLon
        
) {
}
