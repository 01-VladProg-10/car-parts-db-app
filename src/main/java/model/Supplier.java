package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private int id;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;
}