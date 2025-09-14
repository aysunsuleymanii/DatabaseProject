package mk.ukim.finki.easyfood.service.impl;

import mk.ukim.finki.easyfood.model.RestaurantOwner;
import mk.ukim.finki.easyfood.model.enumerations.ROLE;
import mk.ukim.finki.easyfood.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.easyfood.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.easyfood.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.easyfood.repository.RestaurantOwnerRepository;
import mk.ukim.finki.easyfood.service.RestaurantOwnerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {

    private final RestaurantOwnerRepository restaurantOwnerRepository;

    public RestaurantOwnerServiceImpl(RestaurantOwnerRepository restaurantOwnerRepository) {
        this.restaurantOwnerRepository = restaurantOwnerRepository;
    }


    @Override
    public RestaurantOwner findByEmail(String email) {
        return restaurantOwnerRepository.findByEmail(email).orElse(null);
    }

    @Override
    public RestaurantOwner findById(Long id) {
        return restaurantOwnerRepository.findById(id).orElse(null);
    }

    @Override
    public List<RestaurantOwner> findAll() {
        return restaurantOwnerRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        restaurantOwnerRepository.deleteById(id);
    }
}