package giuseppetavella.demo_login_system.domain.entities.client_address_checklists;

import giuseppetavella.demo_login_system.domain.entities.checklists.ChecklistsService;
import giuseppetavella.demo_login_system.domain.entities.client_addresses.ClientAddressesService;
import giuseppetavella.demo_login_system.domain.entities.checklists.Checklist;
import giuseppetavella.demo_login_system.domain.entities.client_addresses.ClientAddress;
import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.exceptions.ClientAddressChecklistException;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientAddressChecklistsService {
    
    @Autowired
    private ClientAddressChecklistsRepository clientAddressChecklistsRepository;
    
    @Autowired
    private ClientAddressesService clientAddressesService;
    
    @Autowired
    private ChecklistsService checklistsService;


    /**
     * Save a checklist associated to a client address
     * (so a client address checklist)
     * 
     * @return
     */
    public ClientAddressChecklist save(ClientAddressChecklist clientAddressChecklist) {
        return this.clientAddressChecklistsRepository.save(clientAddressChecklist);
    }

    /**
     * This checklist-client address association exists already?
     */
    // public boolean existsById(UUID clientAddressChecklistId) {
    //     if(clientAddressChecklistId == null) {
    //         return false;
    //     }
    //     return this.clientAddressChecklistsRepository.existsById(clientAddressChecklistId);
    // }

    
    /**
     * The client address has this checklist? 
     */
    public boolean clientAddressHasChecklist(ClientAddress clientAddress, Checklist checklist) {
        return this.clientAddressChecklistsRepository.clientAddressHasChecklist(clientAddress, checklist);
    }
    
    
    /**
     * Add a checklist to a client address.
     */
    public ClientAddressChecklist addChecklistToClientAddress(UUID checklistId,
                                                              UUID clientAdressId,
                                                              Company company) 
    {
        
        // **********************
        // CHECKLIST & CLIENT ADDRESS EXIST
        // **********************
        
        // find the checklist and the client address
        // they both must belong to this company
        Checklist checklistFromDB = this.checklistsService.findById(checklistId);
        
        ClientAddress clientAddressFromDB = this.clientAddressesService.findById(clientAdressId);


        // **********************
        // CHECKLIST & CLIENT ADDRESS BELONG TO THIS COMPANY
        // **********************

        AuthorizationHelper.requireSameCompany(company, checklistFromDB.getCompany());
        
        AuthorizationHelper.requireSameCompany(company, clientAddressFromDB.getClient().getCompany());

        // **********************
        // THIS CLIENT ADDRESS HAS THIS CHECKLIST ALREADY ASSOCIATED
        // **********************
        
        // check that this checklist was not already associated to this client address
        // checklist must be unique per client address
        if(this.clientAddressHasChecklist(clientAddressFromDB, checklistFromDB)) {
            throw new ClientAddressChecklistException(
                    clientAdressId, 
                    checklistId, 
                    "This checklist is already associated to this client address"
            );
        }
        
        // all good, save this new client address checklist association
        ClientAddressChecklist clientAddressChecklist = new ClientAddressChecklist(
                clientAddressFromDB,
                checklistFromDB
        );
        
        return this.clientAddressChecklistsRepository.save(clientAddressChecklist);
        
    }


}
