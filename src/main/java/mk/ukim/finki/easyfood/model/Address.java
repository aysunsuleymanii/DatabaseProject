package mk.ukim.finki.easyfood.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @OneToMany(mappedBy = "address")
    private List<Order> orders;

    @ManyToMany(mappedBy = "addresses")
    private List<AppUser> users;

    // getters and setters

    @Override
    public String toString() {
        return  city +
                ", " + street +
                ", " + postalCode;
    }

}
