package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.repositories.ClientAddressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientAddressesService {
    
    @Autowired
    private ClientAddressesRepository clientAddressesRepository;
    
}
