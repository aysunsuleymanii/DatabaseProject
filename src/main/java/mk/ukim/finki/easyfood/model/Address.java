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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }
}
