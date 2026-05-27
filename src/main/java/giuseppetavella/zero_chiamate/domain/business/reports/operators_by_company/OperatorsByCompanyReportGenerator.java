package giuseppetavella.zero_chiamate.domain.business.reports.operators_by_company;

import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.UserRole;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorsByCompanyReportGenerator {
    
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private EmailService emailService;


    /**
     * Generate a report with all users of a company in it.
     * 
     * Columns: 
     *    Fullname | 
     *    ----------
     *    
     * @return
     */
    public Csv generate(Company company) {
        
        List<ProfileToSendDTO> users = usersService
                            .findUsersByRole(company, UserRole.OPERATOR)
                            .stream()
                            .map(usersService::toProfileDTO)
                            .toList();
        
        List<String> fields = List.of("Name", "Email", "Company email");
        
        var csv = new Csv(fields);
        
        
        for (var user : users) {
            csv.addRow(
                user.getFirstname(),
                user.getEmail(),
                user.getCompany().getEmail()
            );
        }

        return csv;
    }
    
    
    
}
