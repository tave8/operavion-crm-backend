package giuseppetavella.demo_login_system.runners;


import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.repositories.ShiftsRepository;
import giuseppetavella.demo_login_system.services.ArticlesService;
import giuseppetavella.demo_login_system.services.CompaniesService;
import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataRunner implements CommandLineRunner {

    @Autowired
    private UsersService usersService;
    
    @Autowired
    private ArticlesService articlesService;
    
    @Autowired
    private ShiftsRepository shiftsRepository;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Override
    public void run(String... args) throws Exception {
        
        Company company = this.companiesService.findById("922fb7dd-95cd-4266-aad9-c6f734f8386c");

        LocalDate today = LocalDate.now().minusYears(2);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        List<Object[]> results = this.shiftsRepository.countShiftsByOperator(company, today, tomorrow);

        // System.out.println(operatorsAndCount);

        Map<User, Integer> userCountMap = results.stream()
                .collect(Collectors.toMap(
                        row -> (User) row[0],                 // Key: User entity
                        row -> ((Long) row[1]).intValue(),    // Value: Count as Integer
                        (existing, replacement) -> existing,  // Merge function (safeguard for duplicates)
                        LinkedHashMap::new                    // Keeps the JPQL ORDER BY intact!
                ));
        for(User user : userCountMap.keySet()) {
            System.out.println("user: " + user.getFullname() + " | count: " + userCountMap.get(user));
        }
        //
        // System.out.println(userCountMap);
        
        // System.out.println("hello");
        
        // User user1 = new User(
        //         "giuseppetavella8+@gmail.com",
        //         "1234",
        //         "Giuseppe",
        //         "Tavella"
        // );
        

        // ****** FIND BY ID
        // this.usersService.addUser(user1);
        // User user1FromDB = this.usersService.findById("b9d38a58-a36d-49a0-b353-032a9d47c9f6");

        // System.out.println(user1FromDB);
        //
        // Article article1 = new Article(
        //     user1,
        //     "article 1",
        //     "content"    
        // );
        
        // this.articlesService.addArticle(article1);
        // System.out.println(this.usersService.existsByEmail("giuseppetavella8@gmail.com"));
        
    }
}
