package projekti;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
    @NotEmpty
    @Size(min = 4, max = 50)
    private String userName;
    
    @NotEmpty
    @Size(min = 4, max = 80)
    private String password;
    
    @NotEmpty
    @Size(min = 4, max = 50)
    private String name;
    
    @NotEmpty
    @Size(min = 4, max = 50)
    private String profileName;
    
    @OneToMany(mappedBy = "account")
    private List<ImageObject> images;
    
    private Long profileImageId;
    
    @OneToMany(mappedBy = "account")
    private List<Message> messages;
    
    @OneToMany(mappedBy = "writer")
    private List<Comment> comments;
    
    @ManyToMany(mappedBy = "likedBy")
    private List<ImageObject> likedImages;
    
    @ManyToMany(mappedBy = "likedBy")
    private List<Message> likedMessages;
    
    @ManyToMany(mappedBy = "myFollowers")
    private List<Account> iFollow;
    @ManyToMany
    private List<Account> myFollowers;
}
