package simulator.exceptions;

import simulator.Uranus;

public class OverflowException extends Exception {

    private static final long serialVersionUID = 1L;
    private static String MSG = "Process ended with an overflow.";

    public String toString() {
        Uranus.ui.writeError("Error: " + MSG);
        return MSG;
    }

    public String getMessage() {
        return MSG;
    }
}
