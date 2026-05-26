package giuseppetavella.zero_chiamate.infrastructure.csv;

import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.UserRole;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersCsvGenerationService {
    
    
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
    public Csv generateOperatorsByCompanyReport(Company company) {
        
        List<User> users = usersService.findUsersByRole(company, UserRole.OPERATOR);
        
        String[] fields = {"Fullname"};
        
        var csv = new Csv(fields);
        
        for (var user : users) {
            csv.addRow(
                user.getEmail()
            );
        }
        
        emailService.sendEmail(
                
        );
                
        return csv;
    }
    
    
    // this should return a Csv instance
    // public Csv generateArticlesReport() {
    //
    //     List<Article> articles = this.articlesService.findAll();
    //
    //     String[] fields = {"Author", "Title", "Content"};
    //
    //     CsvGeneratorService csv = new CsvGeneratorService(fields);
    //
    //     for (Article article : articles) {
    //         csv.addRow(
    //             article.getUser().getFirstname(),
    //             article.getTitle(),
    //             article.getContent()
    //         );
    //     }
    //
    //     return new Csv(csv);
    //
    // }
    //
    
}
