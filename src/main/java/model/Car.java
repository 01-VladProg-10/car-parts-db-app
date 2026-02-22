package model;

public class Car {
    private int id;
    private CarModel carModel;
    private String vin;
    private String color;
    private String fuelType;

    public Car() {}
    public Car(int id, CarModel carModel, String vin, String color, String fuelType) {
        this.id = id;
        this.carModel = carModel;
        this.vin = vin;
        this.color = color;
        this.fuelType = fuelType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public CarModel getCarModel() { return carModel; }
    public void setCarModel(CarModel carModel) { this.carModel = carModel; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
}