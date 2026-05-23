package giuseppetavella.zero_chiamate.domain.entities.companies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    /**
     * The company with the given email exists?
     */
    @Query("SELECT COUNT(c) > 0 FROM Company c WHERE LOWER(c.email) = LOWER(:email)")
    Boolean existsByEmail(String email);


}
