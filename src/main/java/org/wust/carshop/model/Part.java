package org.wust.carshop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Part {
    private Integer id;
    private final String carModel;
    private final String carBrand;
    private final String serialNumber;
    private final String producer;
    private final String category;
    private final Double price;
}
