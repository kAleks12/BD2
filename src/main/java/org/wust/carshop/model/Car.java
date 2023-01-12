package org.wust.carshop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Car {
    private Integer id;
    private final String color;
    private final String model;
    private final String VIN;
    private final int productionYear;
    private final Client owner;

}
