package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Address;
import giuseppetavella.demo_login_system.entities.Client;
import giuseppetavella.demo_login_system.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientsService {
    
    @Autowired
    private ClientsRepository clientsRepository;
    
    @Autowired
    private AddressesService addressesService;
    

    /**
     * Add a client with a legal address.
     * 
     * @param client
     * @param legalAddress
     * @return
     */
    @Transactional
    public Client addClientWithLegalAddress(Client client, Address legalAddress) 
    {
    
        // add the legal address
        this.addressesService.add(legalAddress);
        
        //  add the client
        return this.clientsRepository.save(client);
        
    }
    
    
}
