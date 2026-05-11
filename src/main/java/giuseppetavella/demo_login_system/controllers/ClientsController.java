package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.*;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewClientAddressSentDTO;
import giuseppetavella.demo_login_system.payloads.in_request.NewClientSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ClientAddressToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ClientToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.NotificationToSendDTO;
import giuseppetavella.demo_login_system.services.ClientAddressesService;
import giuseppetavella.demo_login_system.services.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientsController {
    
    @Autowired
    private ClientsService clientsService;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    
    /*
    * Get clients.
    * */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<ClientToSendDTO> getClients(@AuthenticationPrincipal User currentUser,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                               // by default, clients are ordered by their legal name ascending: a,b,c...
                                               @RequestParam(value = "sortBy", defaultValue = "legalName") String sortBy,
                                               @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
                                               // you can search clients by legal name
                                               @RequestParam(value = "legalName", defaultValue = "") String legalName) 
    {

        // sortBy must be one of these values
        StringHelper.requireInValues(
                sortBy,
                List.of("legalName"),
                "sortBy"
        );

        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );

        Company company = currentUser.getCompany();

        Page<Client> clientsPage = this.clientsService.findClientsByCompany(
                company,
                legalName,
                page,
                pageSize,
                sortBy,
                sortOrder
        );

        return clientsPage.map(client -> new ClientToSendDTO(client));
        
    }
    
    

    /**
     * Add a client
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ClientToSendDTO addClientWithLegalAddress(@RequestBody @Validated NewClientSentDTO body,
                                                     BindingResult validation,
                                                     @AuthenticationPrincipal User currentUser) 
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        // it's the controller's responsibility to know which 
        // stuff to extract from the payload, and then call the right methods
        // that's why we initialize these objects here in the controller
        
        Company company = currentUser.getCompany();
        
        // create a legal address
        Address legalAddress = new Address(
                body.legalAddressLat(),
                body.legalAddressLon(),
                body.legalAddress()
        );
        
        Client client = new Client(
             company,
             legalAddress,
             body.email(),
             body.legalName(),
             body.vat(),
             body.phone()   
        );
        
        Client clientFromDB = this.clientsService.addClientWithLegalAddress(client, legalAddress);
        
        return new ClientToSendDTO(clientFromDB);
        
        
    }


    /**
     * Add an address to a client.
     */
    @PostMapping("/{clientId}/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ClientAddressToSendDTO addAddressToClient(@RequestBody @Validated NewClientAddressSentDTO body,
                                                     BindingResult validation,
                                                     @AuthenticationPrincipal User currentUser,
                                                     @PathVariable("clientId") String clientIdAsStr)
    {

        PayloadValidationHelper.requireNoErrors(validation);
        
        UUID clientId = StringHelper.parseUUID(clientIdAsStr);

        // create an address
        Address address = new Address(
                body.addressLat(),
                body.addressLon(),
                body.address()
        );
        
        ClientAddress clientAddressFromDB = this.clientAddressesService.addAddressToClient(clientId, address, body.addressName(), currentUser);

        return new ClientAddressToSendDTO(clientAddressFromDB);


    }


    /**
     * Get client addresses of my company.
     */
    @GetMapping("/addresses")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<ClientAddressToSendDTO> getClientAddressesOfMyCompany(@AuthenticationPrincipal User currentUser,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                                   @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder)
    {
        
        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );

        Company company = currentUser.getCompany();

        Page<ClientAddress> clientsAddressesPage = this.clientAddressesService.findClientAddressesByCompany(
                company,
                page,
                pageSize,
                sortOrder
        );

        return clientsAddressesPage.map(clientAddress -> new ClientAddressToSendDTO(clientAddress));


    }
    

    /**
     * Get the addresses of a client.
     */
    @GetMapping("/{clientId}/addresses")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<ClientAddressToSendDTO> getAddressesOfClient(@AuthenticationPrincipal User currentUser,
                                                             @PathVariable("clientId") String clientIdAsStr,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                             // @RequestParam(value = "sortBy", defaultValue = "clientId") String sortBy,
                                                             @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder)
    {

        UUID clientId = StringHelper.parseUUID(clientIdAsStr);
        
        // StringHelper.requireInValues(
        //         sortBy,
        //         List.of("clientId"),
        //         "sortBy"
        // );

        StringHelper.requireInValues(
                sortOrder,
                List.of("asc", "desc"),
                "sortOrder"
        );


        Page<ClientAddress> clientsAddressesPage = this.clientAddressesService.findAddressesByClient(
                currentUser,
                clientId,
                page,
                pageSize,
                // sortBy,
                sortOrder
        );

        return clientsAddressesPage.map(clientAddress -> new ClientAddressToSendDTO(clientAddress));


    }
    
    
}
