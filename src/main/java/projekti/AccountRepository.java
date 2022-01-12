package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserName(String userName);
    Account findByProfileName(String profileName);
    List<Account> findByProfileNameContaining(String profileName);
    Account findByProfileImageId(Long id);
}
