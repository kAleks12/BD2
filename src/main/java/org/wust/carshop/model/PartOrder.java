package org.wust.carshop.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PartOrder {
    private Integer id;
    private final Employee orderingEmployee;
    private final Part orderedPart;
    private final Integer quantity;
    private final Integer price;
    private final LocalDateTime orderDate;
}
