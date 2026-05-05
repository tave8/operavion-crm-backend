package giuseppetavella.demo_login_system.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID itemId) {
        super("The item with ID '" + itemId + "' has not been found.");
    }

    public NotFoundException(Long itemId) {
        super("The item with ID '" + itemId + "' has not been found.");
    }

    public NotFoundException(Long itemId, String entity) {
        super("The item '" + entity + "' with ID " + itemId + " has not been found.");
    }


    public NotFoundException(UUID itemId, String entity) {
        super("The item '" + entity + "' with ID " + itemId + " has not been found.");
    }

    public NotFoundException(String msg) {
        super(msg);
    }

}