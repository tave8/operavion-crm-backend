package giuseppetavella.demo_login_system.domain.entities.addresses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressesService {
    
    @Autowired
    private giuseppetavella.demo_login_system.domain.entities.addresses.AddressesRepository addressesRepository;

    /**
     * Add an address.
     */
    
    public giuseppetavella.demo_login_system.domain.entities.addresses.Address add(giuseppetavella.demo_login_system.domain.entities.addresses.Address address) {
        return this.addressesRepository.save(address);
    }

}
