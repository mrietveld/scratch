package nl.rietveld.reactor;

import java.util.LinkedList;

/**
 * The Queue that holds the Tasks to be executed.
 * The Task Queue is a static object, to which to Handlers push addTask Tasks.
 * The Task Queue is being read by the Worker Threads, which extract the Tasks and executeTask them.
 */
class TaskQueue
{
    protected LinkedList _list;

    /**
     * Creates a new empty TaskQueue object
     */
    public TaskQueue() {
        _list = new LinkedList();
    }

    /**
     * Adds a task to the TaskQueue. This Queue is implemented as unbounded queue, so there's no limit on the quantity of tasks entered.
     * @param task the task to be added to the TaskQueue
     */
    public synchronized void addTask(Task task) {
        _list.add(task);
        notify();
    }

    /**
     * Extracts a Task out of the Task Queue.
     * If the Queue is empty, this method will cause its caller to wait until some Task had been entered to the TaskQueue.
     * @return the next Task to be executed
     * @throws InterruptedException when interrupted
     */
    public synchronized Task getTask() throws InterruptedException {
        while (_list.isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException ie) {
                throw ie;
            }
        }
        return (Task)_list.remove(0);
    }
    
    boolean isEmpty(){return _list.isEmpty();}
}


