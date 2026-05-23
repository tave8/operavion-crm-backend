package giuseppetavella.demo_login_system.domain.entities.companies;

import giuseppetavella.demo_login_system.exceptions.InvalidUUIDStringException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.SignupSentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompaniesService {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Find company by ID.
     */
    public Company findById(UUID companyId) throws NotFoundException {
        return this.companyRepository.findById(companyId).orElseThrow(() -> new NotFoundException(companyId, "company"));
    }

    public Company findById(String companyId) throws NotFoundException {
        try {
            
            return this.findById(UUID.fromString(companyId));
            
        } catch(IllegalArgumentException ex) {
            throw new InvalidUUIDStringException(companyId);
        }
    }

    public Company save(Company company) {
        return this.companyRepository.save(company);
    }

    /**
     * When user signs up, they are the company's owner, 
     * therefore we add the company and associate the user to it.
     */
    public Company addCompany(SignupSentDTO body) 
    {
        if(this.existsByEmail(body.email())) {
            throw new UnauthorizedException("A company with this email already exists.");
        }
        
        Company company = new Company(
                body.legalName(),
                body.email()
        );
        
        return this.companyRepository.save(company);
        
    }


    /**
     * A company with the given email exists?
     */
    public boolean existsByEmail(String email) {
        return this.companyRepository.existsByEmail(email);
    }
    

}
