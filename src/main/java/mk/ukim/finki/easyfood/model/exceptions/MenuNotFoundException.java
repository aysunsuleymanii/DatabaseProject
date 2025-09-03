package mk.ukim.finki.easyfood.model.exceptions;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(Long menuId) {
        super(String.format("Menu with id %d does not exist.", menuId));
    }
}