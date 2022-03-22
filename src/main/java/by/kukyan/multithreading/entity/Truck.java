package by.kukyan.multithreading.entity;

import java.util.concurrent.Callable;

public class Truck implements Callable<String> {
    private String licensePlate;
    private boolean perishableCargo;
    private TruckState state;
    private final int maxCapacity;
    private boolean forUploading;
    private int cargoSize;

    public Truck(){
        this.maxCapacity = 0;
    }

    public Truck(String plate, int capacity, boolean uploading){
        licensePlate = plate;
        maxCapacity = capacity;
        perishableCargo = false;
        state = TruckState.WAITING;
        forUploading = uploading;
        cargoSize = uploading ? 0 : maxCapacity;
    }

    public Truck(String plate, int capacity, boolean uploading, boolean perishable){
        licensePlate = plate;
        maxCapacity = capacity;
        perishableCargo = perishable;
        state = TruckState.WAITING;
        forUploading = uploading;
        cargoSize = uploading ? 0 : maxCapacity;

    }

    @Override
    public String call() throws Exception {
        TruckBase base = TruckBase.getInstance();
        TruckTerminal terminal = base.startLoading(this);
        terminal.load(this);
        return "Truck" + getLicensePlate() + ", terminal" + terminal.getId();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public boolean isPerishableCargo() {
        return perishableCargo;
    }

    public void setPerishableCargo(boolean perishableCargo) {
        this.perishableCargo = perishableCargo;
    }

    public TruckState getState() {
        return state;
    }

    public void setState(TruckState state) {
        this.state = state;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isForUploading() {
        return forUploading;
    }

    public void setForUploading(boolean forUploading) {
        this.forUploading = forUploading;
    }

    public int getCargoSize() {
        return cargoSize;
    }

    public void setCargoSize(int cargoSize) {
        this.cargoSize = cargoSize;
    }
}
