package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Address;
import giuseppetavella.demo_login_system.entities.Client;
import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
import giuseppetavella.demo_login_system.payloads.in_request.NewClientSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.ClientToSendDTO;
import giuseppetavella.demo_login_system.services.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientsController {
    
    @Autowired
    private ClientsService clientsService;
    

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
    
    
}
