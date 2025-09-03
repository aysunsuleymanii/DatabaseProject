package mk.ukim.finki.easyfood.model.exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(Long itemId) {
        super(String.format("Item with id %d does not exist.", itemId));
    }
}
