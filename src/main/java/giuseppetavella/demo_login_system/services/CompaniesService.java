package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;
import giuseppetavella.demo_login_system.payloads.in_request.SignupSentDTO;
import giuseppetavella.demo_login_system.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompaniesService {

    @Autowired
    private CompanyRepository companyRepository;
    
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
