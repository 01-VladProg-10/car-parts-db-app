package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private int id;
    private CarModel carModel;
    private String vin;
    private String color;
    private String fuelType;
}