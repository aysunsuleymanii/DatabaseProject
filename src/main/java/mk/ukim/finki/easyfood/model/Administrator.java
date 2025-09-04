package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;

import java.time.LocalDate;

@Entity
@Table(name = "administrator")
@PrimaryKeyJoinColumn(name = "user_id")
@Data

public class Administrator extends AppUser {

    protected Administrator() {
        super();
    }

    @Column(name = "authorized_from")
    private LocalDate authorizedFrom;

    @Column(name = "authorized_to")
    private LocalDate authorizedTo;

    // getters and setters
    public Administrator(String email, String password, String firstName, String lastName, String phone, ROLE role) {
        super(email, password, firstName, lastName, phone, role);
    }
}
