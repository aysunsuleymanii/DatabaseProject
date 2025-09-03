package mk.ukim.finki.easyfood.model.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(String.format("User with email %s does not exist.", email));
    }
}