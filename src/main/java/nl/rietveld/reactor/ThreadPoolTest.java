package nl.rietveld.reactor;


import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author meisles Created on 01/01/2005
 *  
 */

public class ThreadPoolTest extends TestCase {
    public static final int POOL_SIZE = 10;

    public void testThreadPool() {
        ThreadPool pool = new ThreadPool(POOL_SIZE);
        Assert.assertTrue(
                "Thread pool constructed with non-zero num of threads allive",
                pool.size() == 0);
    }

    public void testStartPool() {
        ThreadPool pool = new ThreadPool(POOL_SIZE);
        pool.startPool();
        Assert.assertTrue(
                "Thread pool started with wrong num of threads allive", pool
                        .size() == POOL_SIZE);

    }

    public void testStopPool() {
        ThreadPool pool = new ThreadPool(POOL_SIZE);
        pool.startPool();
        pool.stopPool();
        pool.join(10);//this waits 10 millisec per Thread
        Assert.assertTrue(
                "Thread pool sttoped and some threads are still allive", pool
                        .size() == 0);
    }

    public void testAddTask() {
        TaskTest task = new TaskTest(false);
        ThreadPool pool = new ThreadPool(POOL_SIZE);
        pool.startPool();
        pool.addTask(task);
        task.join();
        pool.stopPool();
        Assert.assertTrue("Task not Executed", task.flag);
      
    }

    public void testAddTask_Dirty() {
        TaskTest task = new TaskTest(true);
        ThreadPool pool = new ThreadPool(POOL_SIZE);
        pool.startPool();
        pool.addTask(task);
        task.join();
        Assert.assertTrue("Thread died during execution of dirty task", pool
                .size() == POOL_SIZE);
    }
    
    private class TaskTest implements Task{
        private boolean flag, dirty;
        
        public TaskTest(boolean _dirty) {
            flag = false;
            dirty = _dirty;
        }
        /**
         * 
         */
        public void join() { // wait untill task is executed;
            while(!flag) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        public void executeTask() {
            int a = 0;
            flag = true;
            notifyAll();
            if(dirty) a = 5 / a; //throw an unreported exception (devide by zero)
        }
        
    }
}

