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

@Service
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {


}