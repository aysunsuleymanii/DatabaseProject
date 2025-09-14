package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.Restaurant;
import mk.ukim.finki.easyfood.model.RestaurantOwner;

import java.time.LocalTime;
import java.util.List;

public interface RestaurantOwnerService {
    RestaurantOwner findByEmail(String email);

    RestaurantOwner findById(Long id);

    List<RestaurantOwner> findAll();

    void delete(Long id);
}