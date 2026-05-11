package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Address;
import giuseppetavella.demo_login_system.entities.Client;
import giuseppetavella.demo_login_system.entities.ClientAddress;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.repositories.ClientAddressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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


    /**
     * Find client-address associations.
     * 
     * @return
     */
    public Page<ClientAddress> findClientAddresses(UUID clientId,
                                                   int page,
                                                   int pageSize,
                                                   // String sortBy,
                                                   String sortOrder) 
    {

        // // we can sort by these values
        // StringHelper.requireInValues(
        //         sortBy,
        //         List.of("clientId"),
        //         "sortBy"
        // );

        // we can sort in these "directions"
        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );
        
        
        Client clientFromDB = this.clientsService.findById(clientId);

        // how many elements the page has
        int finalSize = Math.clamp(pageSize, 1, 20);

        // at which page we start at  
        int finalPage = Math.max(0, page);

        // Sort sort = sortOrder.equals("asc")
        //         ? Sort.by(sortBy).ascending()
        //         : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(
                finalPage, 
                finalSize 
                // sort
        );

        return this.clientAddressesRepository.findClientAddresses(
                clientFromDB,
                pageable
        );
        
    }
    
}
