package giuseppetavella.zero_chiamate.unit;

import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesRepository;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CompaniesRepositoryTest {

    @Autowired
    private CompaniesRepository repo;

    @AfterEach
    void tearDown() {
        repo.deleteAll();
    }

    @Test
    void existsByEmail() {
        // given
        String email = "giuseppetavella8@gmail.com";
        
        // when
        boolean exists = repo.existsByEmail(email);
        
        // then
        assertThat(exists).isFalse();
    }

    
    @Test
    void canAddCompany() {
        // given
        Company company = new Company(
                "my company", "giuseppetavella8@gmail.com"
        );
        
        // when
        Company companyFromDB = repo.save(company);
        
        // then     
        assertThat(companyFromDB).isNotNull();
        
    }
    
    
}