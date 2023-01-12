package org.wust.carshop.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Repair {
    private Integer id;
    private final Car repairedCar;
    private final Employee mechanic;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer price;
}
