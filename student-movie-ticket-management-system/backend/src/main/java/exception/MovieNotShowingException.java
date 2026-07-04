package exception;

public class MovieNotShowingException extends RuntimeException {
    public MovieNotShowingException(String message) {
        super(message);
    }
}