package by.kukyan.multithreading.entity;

import java.util.concurrent.TimeUnit;

import by.kukyan.multithreading.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TruckTerminal {
    private static final Logger logger = LogManager.getLogger();
    private final int id;
    private final TruckBase base;

    public TruckTerminal(int newId, TruckBase newBase){
        id = newId;
        base = newBase;
    }

    public void load(Truck truck) throws CustomException {
        truck.setState(TruckState.LOADING);
        try {
            TimeUnit.MILLISECONDS.sleep(1200);
        } catch (InterruptedException e) {
            logger.error("error while waiting for full load", e);
            Thread.currentThread().interrupt();
            throw new CustomException("error while waiting for full load", e);
        }
        truck.setCargoSize(truck.isForUploading() ? truck.getMaxCapacity() : 0);
        truck.setState(TruckState.FINISHED);
    }

    public int getId(){
        return id;
    }
}
