package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.*;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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



    /**
     * Find clients of the given company.
     */
    public Page<Client> findClientsByCompany(Company company,
                                                int page,
                                                int pageSize,
                                                String sortBy,
                                                String sortOrder) throws InvalidDataException
    {

        // we can sort by these values
        StringHelper.requireInValues(
                sortBy,
                List.of("legalName"),
                "sortBy"
        );

        // we can sort in these "directions"
        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );

        // how many elements the page has
        int finalSize = Math.clamp(pageSize, 1, 20);

        // at which page we start at  
        int finalPage = Math.max(0, page);
        
        Sort sort = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(finalPage, finalSize, sort);

        return this.clientsRepository.findClientsByCompany(
                company,
                pageable
        );

    }



}
