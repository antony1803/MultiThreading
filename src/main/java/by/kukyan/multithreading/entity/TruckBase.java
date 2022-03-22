package by.kukyan.multithreading.entity;

import by.kukyan.multithreading.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TruckBase {
    private static final Logger logger = LogManager.getLogger();
    private static final int MAX_CAPACITY_OF_THE_BASE = 200;
    private static final double FILING = 0.5;
    private static final double EMPTINESS = 0.1;
    private static TruckBase instance;
    private ReentrantLock lock = new ReentrantLock();
    private Deque<Condition> turn = new ArrayDeque<>();
    private static AtomicBoolean permission = new AtomicBoolean(true);
    private static ReentrantLock instanceLock = new ReentrantLock();


    public static TruckBase getInstance() throws CustomException {
        if(permission.get()){
            try{
                instanceLock.lock();
                if(instance == null){
                    instance = new TruckBase();
                    TimeUnit.MILLISECONDS.sleep(250);
                }
            } catch (InterruptedException e) {
                logger.error("error while sleeping", e);
                throw new CustomException("error while sleeping", e);
            }
            finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }

    public TruckTerminal startLoading(Truck truck){
        return null;
    }

    public void releaseTerminal(){}

}
