package org.wust.carshop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Part {
    private Integer id;
    private final String serialNumber;
    private final String manufacturer;
    private final String category;
    private final Integer stock;
}
