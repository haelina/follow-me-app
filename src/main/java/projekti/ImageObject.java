package projekti;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageObject extends AbstractPersistable<Long> {
    //Uncomment this to make it work with h2 database
    //@Lob
    private byte[] content;
    private String description;
    @ManyToOne
    private Account account;
    @ManyToMany
    private List<Account> likedBy;
    @OneToMany(mappedBy = "image")
    private List<Comment> comments;
}
