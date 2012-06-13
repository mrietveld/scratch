package nl.rietveld.reactor;

/**
 * An exception, used to indicate a Task has failed during execution
 */
public class TaskFailedException extends Exception {

    public TaskFailedException() {
        super();
    }

    public TaskFailedException(String message) {
        super(message);
    }

    public TaskFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}