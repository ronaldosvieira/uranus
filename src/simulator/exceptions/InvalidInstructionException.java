package simulator.exceptions;

/**
 * Created by Ronaldo on 2015-01-15.
 */
public class InvalidInstructionException extends Throwable {
    private String inst;

    public InvalidInstructionException(String inst) {
        this.inst = inst;
    }

    public String toString() {
        return "The following instruction doesn't exist: " + this.inst;
    }

    public String getMessage() {
        return "The following instruction doesn't exist: " + this.inst;
    }
}
