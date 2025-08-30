package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;
import mk.ukim.finki.easyfood.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.easyfood.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.easyfood.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.easyfood.repository.AppUserRepository;
import mk.ukim.finki.easyfood.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AppUser register(String fullName, String email, String phoneNumber, String password, String repeatPassword) {
        if (fullName == null || fullName.isEmpty() ||
                email == null || email.isEmpty() ||
                phoneNumber == null || phoneNumber.isEmpty() ||
                password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new UsernameAlreadyExistsException(email);
        }


        String[] nameParts = fullName.trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        Customer customer = new Customer(email, passwordEncoder.encode(password), firstName, lastName, phoneNumber, ROLE.CUSTOMER);

        return userRepository.save(customer);
    }
}
