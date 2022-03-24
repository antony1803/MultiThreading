package by.kukyan.multithreading.entity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import by.kukyan.multithreading.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TruckTerminal {
    private static final Logger logger = LogManager.getLogger();
    private final int id;

    public TruckTerminal(int newId){
        id = newId;
    }

    public void uploadUnload(Truck truck) throws CustomException {
        truck.setState(TruckState.LOADING);
        try {
            TimeUnit.MILLISECONDS.sleep(truck.isForUploading() ? 12 * (truck.getMaxCapacity() - truck.getCargoSize() + 10) : 12 * truck.getCargoSize() + 10);
        } catch (InterruptedException e) {
            logger.error("error while waiting for full load", e);
            Thread.currentThread().interrupt();
            throw new CustomException("error while waiting for full load", e);
        }
        LogisticsBase base = LogisticsBase.getInstance();
        if(truck.isForUploading()){
            base.checkCargoLeftOnBase();
            base.startLoading(truck);
            base.unloadCargoOnBase(truck.getMaxCapacity() - truck.getCargoSize());
        }
        else {
            base.checkCargoLeftOnBase();
            base.startLoading(truck);
            base.uploadCargoOnBase(truck.getCargoSize());
        }
        truck.setCargoSize(truck.isForUploading() ? truck.getMaxCapacity() : 0);
        truck.setState(TruckState.FINISHED);
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "TruckTerminal{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckTerminal terminal = (TruckTerminal) o;
        return id == terminal.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
