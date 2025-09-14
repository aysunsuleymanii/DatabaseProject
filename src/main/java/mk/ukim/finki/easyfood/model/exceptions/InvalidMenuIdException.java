package mk.ukim.finki.easyfood.model.exceptions;

public class InvalidMenuIdException extends Exception {
    public InvalidMenuIdException() {
        super("Invalid menu %d id exception");
    }
}



