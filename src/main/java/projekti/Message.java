package projekti;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractPersistable<Long> {
    @ManyToOne
    private Account account;
    
    //Uncomment this to make it work with h2 database
    @Column(columnDefinition="TEXT")
    private String message;
    
    private LocalDateTime published;
    
    @OneToMany(mappedBy = "message")
    private List<Comment> comments;
    
    @ManyToMany
    private List<Account> likedBy;
}
