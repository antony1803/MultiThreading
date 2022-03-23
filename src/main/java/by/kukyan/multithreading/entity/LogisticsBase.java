package by.kukyan.multithreading.entity;

import by.kukyan.multithreading.exception.CustomException;
import by.kukyan.multithreading.id.TerminalIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LogisticsBase {
    private static final Logger logger = LogManager.getLogger();
    private static final int BASE_TERMINAL_SIZE = 4;
    private static final int MAX_CAPACITY_OF_THE_BASE_IN_TRUCKS = 200;
    private static final int MAX_CAPACITY_CARGO = 20000;
    private static final double NEED_SOME_SPACE = 0.9;
    private static final double NEED_NEW_CARGO = 0.1;
    private static LogisticsBase instance;
    private ReentrantLock lock = new ReentrantLock();
    private Deque<Condition> sequence = new ArrayDeque<>();
    private Deque<TruckTerminal> freeTerminals = new ArrayDeque<>(BASE_TERMINAL_SIZE);
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static ReentrantLock instanceLock = new ReentrantLock();
    private Condition waitToUploadCargo = lock.newCondition();
    private Condition waitToUnloadCargo = lock.newCondition();
    private Condition waitToGoIntoTheBase = lock.newCondition();
    private int availableCargo;
    private int numberOfTrucksCurrentlyWaitingOnBase;

    private LogisticsBase(){
        for(int i = 0; i < BASE_TERMINAL_SIZE; i++){
            freeTerminals.add(new TruckTerminal(TerminalIdGenerator.getId()));
        }
        availableCargo = 10000;
        numberOfTrucksCurrentlyWaitingOnBase = 0;
    }

    public static LogisticsBase getInstance() throws CustomException {
        if(isCreated.get()){
            try{
                instanceLock.lock();
                if(instance == null){
                    instance = new LogisticsBase();
                    isCreated.set(true);
                }
            }
            finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }

    public void addTruck() throws CustomException{
        lock.lock();
        try {
            while (MAX_CAPACITY_OF_THE_BASE_IN_TRUCKS > numberOfTrucksCurrentlyWaitingOnBase){
                waitToGoIntoTheBase.await();
            }
        } catch (InterruptedException e) {
            logger.error("error while waiting till base can be uploaded with some cargo", e);
        }finally {
            lock.unlock();
        }
    }

    public TruckTerminal startLoading(Truck truck){
        TruckTerminal terminal = null;
        try {
            lock.lock();
            Condition condition = lock.newCondition();
            if(freeTerminals.isEmpty()){
                sequence.addLast(condition);
                condition.await();
            }
            terminal = freeTerminals.removeFirst();
        } catch (InterruptedException e) {
            logger.error("error while waiting for free terminal", e);
            Thread.currentThread().interrupt();
        }finally {
            lock.unlock();
        }
        return terminal;
    }

    public void releaseTerminal(TruckTerminal terminal){
        Condition condition = null;
        try {
            lock.lock();
            freeTerminals.addLast(terminal);
            condition = sequence.pollFirst();
        }finally {
            if(condition != null) {
                condition.signal();
            }
            lock.unlock();
        }
    }

    public void checkCargoLeftOnBase(){
        lock.lock();
        try {
            if(availableCargo > NEED_SOME_SPACE * MAX_CAPACITY_CARGO){
                availableCargo *= 0.8;
            }
            else if(availableCargo < NEED_NEW_CARGO * MAX_CAPACITY_CARGO){
                availableCargo += MAX_CAPACITY_CARGO * 0.2;
            }
        }finally {
            lock.unlock();
        }
    }

    public void uploadCargoOnBase(int cargo){
        lock.lock();
        try {
            while (availableCargo + cargo > MAX_CAPACITY_CARGO){
                waitToUploadCargo.await();
            }
        } catch (InterruptedException e) {
            logger.error("error while waiting till base can be uploaded with some cargo", e);
        }finally {
            lock.unlock();
        }
    }

    public void unloadCargoOnBase(int cargo){
        lock.lock();
        try {
            while (availableCargo - cargo < 0){
                waitToUnloadCargo.await();
            }
        } catch (InterruptedException e) {
            logger.error("error while waiting till base have cargo needed to upload truck", e);
        }finally {
            lock.unlock();
        }
    }

}
