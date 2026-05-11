package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Address;
import giuseppetavella.demo_login_system.repositories.AddressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressesService {
    
    @Autowired
    private AddressesRepository addressesRepository;

    /**
     * Add an address.
     */
    
    public Address add(Address address) {
        return this.addressesRepository.save(address);
    }

}
