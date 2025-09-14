package mk.ukim.finki.easyfood.service.impl;


import jakarta.annotation.PostConstruct;
import mk.ukim.finki.easyfood.model.*;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;
import mk.ukim.finki.easyfood.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserFactory {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<ROLE, UserTypeConfiguration> configurations = new HashMap<>();

    @PostConstruct
    public void initialize() {
        registerUserType(ROLE.CUSTOMER, Customer.class, "customerRepository");
        registerUserType(ROLE.RESTAURANT_OWNER, RestaurantOwner.class, "restaurantOwnerRepository");
        registerUserType(ROLE.DELIVERY_MAN, DeliveryMan.class, "deliveryManRepository");
        registerUserType(ROLE.ADMIN, AppUser.class, "appUserRepository");
    }

    private void registerUserType(ROLE role, Class<? extends AppUser> userClass, String repositoryBeanName) {
        configurations.put(role, new UserTypeConfiguration(userClass, repositoryBeanName));
    }

    public AppUser createUser(String email, String encodedPassword, String firstName,
                              String lastName, String phoneNumber, ROLE role) {

        UserTypeConfiguration config = configurations.get(role);
        if (config == null) {
            throw new IllegalArgumentException("Unsupported role: " + role);
        }

        return config.createAndSave(email, encodedPassword, firstName, lastName, phoneNumber, role);
    }

    public void addUserType(ROLE role, Class<? extends AppUser> userClass, String repositoryBeanName) {
        registerUserType(role, userClass, repositoryBeanName);
    }

    private class UserTypeConfiguration {
        private final Class<? extends AppUser> userClass;
        private final String repositoryBeanName;

        public UserTypeConfiguration(Class<? extends AppUser> userClass, String repositoryBeanName) {
            this.userClass = userClass;
            this.repositoryBeanName = repositoryBeanName;
        }

        @SuppressWarnings("unchecked")
        public AppUser createAndSave(String email, String encodedPassword, String firstName,
                                     String lastName, String phoneNumber, ROLE role) {
            try {
                // Create user instance
                Constructor<? extends AppUser> constructor = userClass.getDeclaredConstructor(
                        String.class, String.class, String.class, String.class, String.class, ROLE.class
                );
                AppUser user = constructor.newInstance(email, encodedPassword, firstName, lastName, phoneNumber, role);

                JpaRepository<AppUser, Long> repository =
                        (JpaRepository<AppUser, Long>) applicationContext.getBean(repositoryBeanName);

                return repository.save(user);

            } catch (Exception e) {
                throw new RuntimeException("Failed to create user of type: " + userClass.getSimpleName(), e);
            }
        }
    }
}