package giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.sent;

import jakarta.validation.constraints.NotNull;

public record NewClientAddressSentDTO(
        
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
