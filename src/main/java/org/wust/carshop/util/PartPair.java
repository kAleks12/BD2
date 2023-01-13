package org.wust.carshop.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.wust.carshop.model.Part;

@AllArgsConstructor
@Getter
public class PartPair {
    private final Part part;
    private final Integer quantity;

}
