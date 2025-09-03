package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;

import java.util.List;

@Entity
@Table(name = "app_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class AppUser {
    protected AppUser() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone", length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    private ROLE role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_addresses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> addresses;


    public AppUser(String email, String password, String firstName, String lastName, String phone, ROLE role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }


}
