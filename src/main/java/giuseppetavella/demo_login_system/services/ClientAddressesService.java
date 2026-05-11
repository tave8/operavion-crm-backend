package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Address;
import giuseppetavella.demo_login_system.entities.Client;
import giuseppetavella.demo_login_system.entities.ClientAddress;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.repositories.ClientAddressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ClientAddressesService {
    
    @Autowired
    private ClientAddressesRepository clientAddressesRepository;
    
    @Autowired
    private ClientsService clientsService;
    
    @Autowired
    private AddressesService addressesService;

    
    /**
     * Add a client-address association.
     */
    @Transactional
    public ClientAddress addAddressToClient(UUID clientId, 
                                            Address address, 
                                            String addressName,
                                            User currentUser) 
    {
        Client clientFromDB = this.clientsService.findById(clientId);
        
        // save address in DB
        Address addressFromDB = this.addressesService.add(address);
        
        // is this client a client of my company
        AuthorizationHelper.requireSameCompany(currentUser.getCompany(), clientFromDB.getCompany());
        
        ClientAddress clientAddress = new ClientAddress(
                clientFromDB,
                addressFromDB,
                addressName
        );
        
        // save client-address association in DB
        ClientAddress clientAddressFromDB = this.clientAddressesRepository.save(clientAddress);
        
        return clientAddressFromDB;
        
    }
    
}
