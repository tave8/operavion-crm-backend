package giuseppetavella.zero_chiamate.unit.domain.companies;

import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesRepository;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
// by default, JPA looks for an in-memory DB, but since
// I'm using another postgres DB just for testing, I have to
// explicitly tell JPA not to scan for an in-memory DB
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompaniesRepositoryTest {

    
    @Autowired
    private CompaniesRepository underTest;
    

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    
    @Test
    void existsByEmail() {
        // given
        String email = "giuseppetavella8@gmail.com";
        
        // when
        boolean exists = underTest.existsByEmail(email);
        
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
        Company companyFromDB = underTest.save(company);
        
        // then     
        assertThat(companyFromDB).isNotNull();
        
    }
    
    
}