package nl.rietveld.reactor;

/**
 * A task that can be pushed to the WorkingQueue, and executed by the Worker Threads
 */
public interface Task {

    /**
     * Executes the Task. Called by the Worker Threads that are in the Thread Pool.
     */
    public void executeTask() throws TaskFailedException;
}
