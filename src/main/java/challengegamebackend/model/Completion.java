package challengegamebackend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Completion {

    @EmbeddedId
    private CompletionId id = new CompletionId();

    @ManyToOne
    @JoinColumn(name = "userId", insertable=false, updatable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "challengId", insertable=false, updatable=false)
    private Challenge challenge;

    private String imgPath;

    private String comment;
}
