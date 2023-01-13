package org.wust.carshop.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {
    private Integer id;
    private final String position;
    private final String name;
    private final String surname;
    private final String login;
    private final String password;
}
