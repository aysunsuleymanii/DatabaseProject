package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Customer;

import java.util.Optional;

public interface UserService {
    AppUser register(String fullName, String email, String phoneNumber, String password, String repeatPassword);

    Customer getCustomerById(Long id);

    Optional<Customer> findByEmail(String email);

    public Customer save(Customer customer);
}

