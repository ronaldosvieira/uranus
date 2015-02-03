package simulator.exceptions;

/**
 * Created by Ronaldo on 2015-01-15.
 */
public class UnableToParseException extends Throwable {
    private String line;

    public UnableToParseException(String line) {
        this.line = line;
    }

    public String toString() {
        return "Unable to parse the following line: " + this.line;
    }

    public String getMessage() {
        return "Unable to parse the following line: " + this.line;
    }
}
