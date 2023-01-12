package org.wust.carshop;

import lombok.Builder;
import lombok.Getter;


/**
 * POJO which stores names of all tables required by the app to work.
 * To create default config use:
 * <pre>
 * TableConfig.builder().build();
 * </pre>
 */
@Builder
@Getter
public class TableConfig {
    @Builder.Default
    private final String carsTable = "samochody";
    @Builder.Default

    private final String carModelsTable = "modele";
    @Builder.Default
    private final String carManufacturersTable = "marki";
    @Builder.Default
    private final String carColorsTable = "kolory";
    @Builder.Default
    private final String clientsTable = "klienci";
    @Builder.Default
    private final String addressesTable = "adresy";
    @Builder.Default
    private final String employeesTable = "pracownicy";
    @Builder.Default
    private final String employeePositionsTable = "stanowiska";
    @Builder.Default
    private final String partTable = "czesci";
    @Builder.Default
    private final String partTypeTable = "typy";
    @Builder.Default
    private final String partManufacturerTable = "producenci";
    @Builder.Default
    private final String partStockTable = "stan_magazywnowy";
    @Builder.Default
    private final String templateRequiredPartsTable = "wymagane_czesci";
    @Builder.Default
    private final String repairUsedPartsTable = "uzyte_czesci";
    @Builder.Default
    private final String templateRepairsTable = "szablony_napraw";
    @Builder.Default
    private final String repairsTable = "naprawy";
    @Builder.Default
    private final String partOrdersTable = "zamowienia";
}
