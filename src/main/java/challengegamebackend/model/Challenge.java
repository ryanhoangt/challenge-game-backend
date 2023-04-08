package challengegamebackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    private String imgPath;

    @Column(nullable = false)
    private long categoryId;
}
