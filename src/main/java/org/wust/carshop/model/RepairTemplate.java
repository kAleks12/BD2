package org.wust.carshop.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RepairTemplate {
    private Integer id;
    private final int cost;
    private final String name;
    private final List<Part> requiredParts;
}
