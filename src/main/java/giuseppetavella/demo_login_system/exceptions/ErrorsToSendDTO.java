package giuseppetavella.demo_login_system.exceptions;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErrorsToSendDTO {

    private final String message;
    private final OffsetDateTime timestamp;
    private final List<String> errors;

    public ErrorsToSendDTO(String message,
                           OffsetDateTime timestamp,
                              List<String> errors)
    {
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public ErrorsToSendDTO(String message,
                              List<String> errors)
    {
        this(message, OffsetDateTime.now(), errors);
    }

    public ErrorsToSendDTO(String message,
                           OffsetDateTime timestamp)
    {
        this(message, timestamp, new ArrayList<>());
    }


    public ErrorsToSendDTO(String message)
    {
        this(message, OffsetDateTime.now(), new ArrayList<>());
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

}