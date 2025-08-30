package mk.ukim.finki.easyfood.service;

import mk.ukim.finki.easyfood.model.AppUser;

public interface UserService {
    AppUser register(String fullName, String email, String phoneNumber, String password, String repeatPassword);
}

