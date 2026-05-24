package giuseppetavella.zero_chiamate.domain.entities.companies;

import giuseppetavella.zero_chiamate.exceptions.InvalidUUIDStringException;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.exceptions.UnauthorizedException;
import giuseppetavella.zero_chiamate.domain.business.auth.dto.sent.SignupSentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CompaniesService {

    @Autowired
    private CompaniesRepository companiesRepository;

    /**
     * Find company by ID.
     */
    public Company getById(UUID companyId) throws NotFoundException {
        return this.companiesRepository.findById(companyId).orElseThrow(() -> new NotFoundException(companyId, "company"));
    }

    public Company getById(String companyId) throws NotFoundException {
        try {
            
            return this.getById(UUID.fromString(companyId));
            
        } catch(IllegalArgumentException ex) {
            throw new InvalidUUIDStringException(companyId);
        }
    }


    public Optional<Company> findByStripeCustomerId(String stripeCustomerId) {
        return companiesRepository.findByStripeCustomerId(stripeCustomerId);
    }
    

    public Company save(Company company) {
        return this.companiesRepository.save(company);
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
        
        return this.companiesRepository.save(company);
        
    }


    /**
     * A company with the given email exists?
     */
    public boolean existsByEmail(String email) {
        return this.companiesRepository.existsByEmail(email);
    }
    

}
