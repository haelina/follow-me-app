package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageObjectRepository extends JpaRepository<ImageObject, Long> {
    ImageObject findByAccountAndId(Account account, Long id);
}
