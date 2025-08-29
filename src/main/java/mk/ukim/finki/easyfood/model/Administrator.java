package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "administrator")
@PrimaryKeyJoinColumn(name = "user_id")
public class Administrator extends AppUser {

    @Column(name = "authorized_from")
    private LocalDate authorizedFrom;

    @Column(name = "authorized_to")
    private LocalDate authorizedTo;

    // getters and setters
}
