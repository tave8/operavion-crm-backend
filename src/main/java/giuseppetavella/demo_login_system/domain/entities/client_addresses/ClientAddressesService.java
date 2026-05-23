package giuseppetavella.demo_login_system.domain.entities.client_addresses;

import giuseppetavella.demo_login_system.domain.entities.addresses.Address;
import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.domain.entities.clients.Client;
import giuseppetavella.demo_login_system.exceptions.InvalidUUIDStringException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.domain.entities.client_addresses.dto.to_send.ClientAddressToSendDTO;
import giuseppetavella.demo_login_system.domain.entities.contract_expectations.dto.to_send.ContractExpectationToSendDTO;
import giuseppetavella.demo_login_system.domain.entities.addresses.AddressesService;
import giuseppetavella.demo_login_system.domain.entities.clients.ClientsService;
import giuseppetavella.demo_login_system.domain.entities.contract_expectations.ContractExpectationsService;
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
    
    @Autowired
    private ContractExpectationsService contractExpectationsService;

    /**
     * Client address -> client address DTO
     * 
     * @param clientAddress
     * @return
     */
    public ClientAddressToSendDTO toClientAddressDTO(ClientAddress clientAddress)
    {
        ContractExpectationToSendDTO contractExpectationToSendDTO = this.contractExpectationsService
                .findByClientAddressDTO(clientAddress);

        return new ClientAddressToSendDTO(
                clientAddress,
                contractExpectationToSendDTO
        );

    }

    /**
     * Client addresses -> client address DTOs
     *
     * @return
     */
    public List<ClientAddressToSendDTO> toClientAddressDTOs(List<ClientAddress> clientAddresses)
    {
        
        return clientAddresses
                .stream()
                .map(ca -> {
                    ContractExpectationToSendDTO contractExpectationToSendDTO = this.contractExpectationsService.findByClientAddressDTO(ca);
                    return new ClientAddressToSendDTO(
                            ca,
                            contractExpectationToSendDTO
                    );
                }).toList();

    }

    /**
     * Find client address by ID.
     */
    public ClientAddress findById(UUID clientAddressId) throws NotFoundException {
        return this.clientAddressesRepository
                .findById(clientAddressId)
                .orElseThrow(() -> new NotFoundException(clientAddressId, "client address"));
    }

    /**
     * Find client address by ID.
     */
    public ClientAddress findById(String clientAddressId) throws NotFoundException {
        try {
            
            return this.findById(UUID.fromString(clientAddressId));
            
        } catch (IllegalArgumentException ex) {
            throw new InvalidUUIDStringException(clientAddressId);
        }
    }

    public ClientAddress findByIdDTO(String clientAddressId) throws NotFoundException {
        try {

            return this.findById(UUID.fromString(clientAddressId));

        } catch (IllegalArgumentException ex) {
            throw new InvalidUUIDStringException(clientAddressId);
        }
    }
    

    
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
     * Find all client address of company.
     * 
     * @return
     */
    public List<ClientAddressToSendDTO> findAllClientAddressesByCompany(Company company)
    {
        return toClientAddressDTOs(
                clientAddressesRepository.findAllClientAddressesByCompany(company)
        );
    }
    

    /**
     * Get client addresses of company.
     */
    public Page<ClientAddress> findClientAddressesByCompany(Company company,
                                                            String searchQuery,
                                                            int page,
                                                            int pageSize,
                                                            String sortOrder,
                                                            String sortBy)
    {

        StringHelper.requireInValues(
                sortBy,
                List.of("addressName"),
                "sortOrder"
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

        Pageable pageable = PageRequest.of(
                finalPage,
                finalSize,
                sort
        );

        String searchQueryPattern = null;

        if(!searchQuery.trim().isEmpty()) {
            String searchQueryCleaned = searchQuery.toLowerCase().trim();
            searchQueryPattern = "%" + searchQueryCleaned + "%";
        }

        return this.clientAddressesRepository.findClientAddressessByCompany(
                company,
                searchQueryPattern,
                pageable
        );

    }
    

    /**
     * Get addresses of client.
     * 
     * @return
     */
    public Page<ClientAddress> findAddressesByClient(User currentUser,
                                                       UUID clientId,
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
        
        // make sure that this client belongs to the same
        // company as the current user
        AuthorizationHelper.requireSameCompany(clientFromDB.getCompany(), currentUser.getCompany());

        // how many elements the page has
        int finalSize = Math.clamp(pageSize, 1, 100);

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

        return this.clientAddressesRepository.findAddressesByClient(
                clientFromDB,
                pageable
        );
        
    }
    
}
