package challengegamebackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Tag {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String name;
}
