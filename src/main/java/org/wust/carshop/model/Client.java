package org.wust.carshop.model;


import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Client {
    private Integer id;
    private final Address address;
    private final String name;
    private final String surname;
}
