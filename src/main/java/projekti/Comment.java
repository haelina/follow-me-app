package projekti;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends AbstractPersistable<Long> {
    @ManyToOne
    private ImageObject image;

    @ManyToOne
    private Message message;

    //Uncomment this to make it work with h2 database
    //@Column(columnDefinition="TEXT")
    private String comment;

    @ManyToOne
    private Account writer;

    private LocalDateTime published;
}
