package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.RestaurantOwner;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;

import java.util.Optional;

public interface UserService {
    AppUser register(String fullName, String email, String phoneNumber, String password, String repeatPassword);

    Customer getCustomerById(Long id);

    Optional<Customer> findByEmail(String email);

    Optional<DeliveryMan> findByEmailDM(String email);

    public Customer save(Customer customer);

    DeliveryMan getDeliveryManById(Long id);

    Optional<DeliveryMan> findDeliveryManByEmail(String email);

    DeliveryMan save(DeliveryMan deliveryMan);

    AppUser registerUserWithRole(String fullName, String email, String phoneNumber,
                                 String password, String repeatPassword, ROLE role);

    Optional<RestaurantOwner> findRestaurantOwnerByEmail(String email);
    RestaurantOwner save(RestaurantOwner restaurantOwner);
}

