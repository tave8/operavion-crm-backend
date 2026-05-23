package giuseppetavella.zero_chiamate.domain.entities.addresses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressesService {
    
    @Autowired
    private giuseppetavella.zero_chiamate.domain.entities.addresses.AddressesRepository addressesRepository;

    /**
     * Add an address.
     */
    
    public giuseppetavella.zero_chiamate.domain.entities.addresses.Address add(giuseppetavella.zero_chiamate.domain.entities.addresses.Address address) {
        return this.addressesRepository.save(address);
    }

}
