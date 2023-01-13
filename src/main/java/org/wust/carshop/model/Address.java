package org.wust.carshop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private Integer id;
    private final String city;
    private final String postalCode;
    private final String street;
    private final String buildingNumber;
    private final Integer apartment;
}
