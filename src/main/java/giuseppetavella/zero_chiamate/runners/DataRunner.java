package giuseppetavella.zero_chiamate.runners;


import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.shifts.ShiftsService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataRunner implements CommandLineRunner {

    @Autowired
    private UsersService usersService;
    
    
    @Autowired
    private ShiftsService shiftsService;
    
    @Autowired
    private CompaniesService companiesService;
    
    @Override
    public void run(String... args) throws Exception {
        

        // ContractExpectationState currentState = null;
        // ContractExpectationState desiredState = ContractExpectationState.PENDING;
        //
        // boolean noStateYet = currentState == null;
        //
        // // first states
        // List<ContractExpectationState> firstStates = List.of(ContractExpectationState.PENDING);
        //
        // // state map
        // Map<ContractExpectationState, List<ContractExpectationState>> stateMap = Map.of(
        //         ContractExpectationState.PENDING, List.of(ContractExpectationState.SUCCESS, ContractExpectationState.FAILED),
        //         ContractExpectationState.SUCCESS, List.of(),
        //         // if processing failed, we can still re-process it
        //         ContractExpectationState.FAILED, List.of(ContractExpectationState.PENDING)
        // );
        //
        // // check if valid state transition
        // DataValidationHelper.requireValidStateTransition(
        //         ContractExpectationState.class,
        //         currentState,
        //         desiredState,
        //         firstStates,
        //         stateMap,
        //         noStateYet,
        //         "contract expectation"
        // );
        //
        // System.out.println("test passed");

        

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
