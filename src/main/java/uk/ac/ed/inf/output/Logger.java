package uk.ac.ed.inf.output;

/**
 * Logger class for logging success messages.
 */
public class Logger {
    // ANSI color codes for displaying text in green
    private final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructor for the Logger class.
     */
    public Logger() {}

    /**
     * Log a success message to the console.
     *
     * @param message The success message to be logged.
     */
    public void logSuccess(Enum message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    /**
     * Log a success message with additional details to the console.
     *
     * @param message           The success message to be logged.
     * @param additionalMessage Additional details to be logged along with the success message.
     */
    public void logSuccess(Enum message, String additionalMessage) {
        System.out.println(ANSI_GREEN + message + " " + additionalMessage + ANSI_RESET);
    }
}
