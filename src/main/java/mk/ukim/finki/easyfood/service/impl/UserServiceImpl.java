package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.Customer;
import mk.ukim.finki.easyfood.model.DeliveryMan;
import mk.ukim.finki.easyfood.model.RestaurantOwner;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;
import mk.ukim.finki.easyfood.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.easyfood.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.easyfood.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.easyfood.repository.AppUserRepository;
import mk.ukim.finki.easyfood.repository.CustomerRepository;
import mk.ukim.finki.easyfood.repository.DeliveryManRepository;
import mk.ukim.finki.easyfood.repository.RestaurantOwnerRepository;
import mk.ukim.finki.easyfood.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final DeliveryManRepository deliveryManRepository;
    private final UserFactory userFactory;

    public UserServiceImpl(AppUserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CustomerRepository customerRepository,
                           RestaurantOwnerRepository restaurantOwnerRepository,
                           DeliveryManRepository deliveryManRepository,
                           UserFactory userFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.restaurantOwnerRepository = restaurantOwnerRepository;
        this.deliveryManRepository = deliveryManRepository;
        this.userFactory = userFactory;
    }

    private void validateCommonFields(String fullName, String email, String phoneNumber, String password, String repeatPassword) {
        if (fullName == null || fullName.isEmpty() ||
                email == null || email.isEmpty() ||
                phoneNumber == null || phoneNumber.isEmpty() ||
                password == null || password.isEmpty()) {
            throw new InvalidArgumentsException("Cannot be validated!");
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new UsernameAlreadyExistsException(email);
        }
    }

    private String[] parseFullName(String fullName) {
        String[] nameParts = fullName.trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        return new String[]{firstName, lastName};
    }

    @Override
    public AppUser register(String fullName, String email, String phoneNumber, String password, String repeatPassword) {
        return registerUserWithRole(fullName, email, phoneNumber, password, repeatPassword, ROLE.CUSTOMER);
    }

    @Override
    public AppUser registerUserWithRole(String fullName, String email, String phoneNumber,
                                        String password, String repeatPassword, ROLE role) {
        validateCommonFields(fullName, email, phoneNumber, password, repeatPassword);

        String[] names = parseFullName(fullName);
        String firstName = names[0];
        String lastName = names[1];
        String encodedPassword = passwordEncoder.encode(password);

        return userFactory.createUser(email, encodedPassword, firstName, lastName, phoneNumber, role);
    }


    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Optional<DeliveryMan> findByEmailDM(String email) {
        return deliveryManRepository.findByEmail(email);
    }

    @Override
    public DeliveryMan getDeliveryManById(Long id) {
        return deliveryManRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery man not found with id: " + id));
    }

    @Override
    public Optional<DeliveryMan> findDeliveryManByEmail(String email) {
        return deliveryManRepository.findByEmail(email);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public DeliveryMan save(DeliveryMan deliveryMan) {
        return deliveryManRepository.save(deliveryMan);
    }

    @Override
    public Optional<RestaurantOwner> findRestaurantOwnerByEmail(String email) {
        return restaurantOwnerRepository.findByEmail(email);
    }

    @Override
    public RestaurantOwner save(RestaurantOwner restaurantOwner) {
        return restaurantOwnerRepository.save(restaurantOwner);
    }
}