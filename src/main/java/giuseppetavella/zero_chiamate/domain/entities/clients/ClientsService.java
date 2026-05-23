package giuseppetavella.zero_chiamate.domain.entities.clients;

import giuseppetavella.zero_chiamate.domain.entities.addresses.Address;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.helpers.StringHelper;
import giuseppetavella.zero_chiamate.domain.entities.addresses.AddressesService;
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
public class ClientsService {
    
    @Autowired
    private ClientsRepository clientsRepository;
    
    @Autowired
    private AddressesService addressesService;
    
    
    public Client findById(UUID clientId) {
        return this.clientsRepository.findById(clientId).orElseThrow(() -> new NotFoundException(clientId, "CLIENT"));
    }

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
                                             String legalName,
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
        int finalSize = Math.clamp(pageSize, 1, 100);

        // at which page we start at  
        int finalPage = Math.max(0, page);
        
        Sort sort = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(finalPage, finalSize, sort);
        
        // we assume there's no legal name
        String legalNamePattern = null;
        
        // create pattern for legal name, if a legal name was specified
        if(!legalName.trim().isEmpty()) {
            legalNamePattern = "%" + legalName.toLowerCase().trim() + "%";
        }

        return this.clientsRepository.findClientsByCompany(
                company,
                legalNamePattern,
                pageable
        );

    }



}
