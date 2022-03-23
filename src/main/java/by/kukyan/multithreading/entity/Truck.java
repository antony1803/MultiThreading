package by.kukyan.multithreading.entity;

import by.kukyan.multithreading.id.PlateIdGenerator;

import java.util.concurrent.Callable;

public class Truck implements Callable<String> {
    private static final int COMMON_CAPACITY = 40;
    private String licensePlate;
    private boolean perishableCargo;
    private TruckState state;
    private final int maxCapacity;
    private boolean forUploading;
    private int cargoSize;

    public Truck(){
        licensePlate = PlateIdGenerator.getId();
        maxCapacity = COMMON_CAPACITY;
        perishableCargo = false;
        state = TruckState.WAITING;
        forUploading = true;
        cargoSize = 0;
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

    public Truck(String plate, int capacity, boolean uploading, boolean perishable, int cargo){
        licensePlate = plate;
        maxCapacity = capacity;
        perishableCargo = perishable;
        state = TruckState.WAITING;
        forUploading = uploading;
        cargoSize = cargo;
    }

    @Override
    public String call() throws Exception {
        LogisticsBase base = LogisticsBase.getInstance();
        TruckTerminal terminal = base.startLoading(this);
        terminal.uploadUnload(this);
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Truck {");
        builder.append("licensePlate = ")
                .append(licensePlate)
                .append(", perishableCargo = ")
                .append(perishableCargo)
                .append(", state = ")
                .append(state)
                .append(", maxCapacity = ")
                .append(maxCapacity)
                .append(", forUploading = ")
                .append(forUploading)
                .append(", cargoSize = ")
                .append(cargoSize)
                .append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return perishableCargo == truck.perishableCargo && maxCapacity == truck.maxCapacity && forUploading == truck.forUploading && cargoSize == truck.cargoSize && licensePlate.equals(truck.licensePlate) && state == truck.state;
    }

    @Override
    public int hashCode() {
        return licensePlate.hashCode() + (perishableCargo ? 1 : 0) + state.hashCode() + maxCapacity + (forUploading ? 1 : 0) + cargoSize;
    }
}
