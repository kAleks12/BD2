package org.wust.carshop.query;

public class UpdateQueries {
    static public final String DELETE_EMPLOYEE = """
           DELETE FROM pracownicy WHERE id = :id
            """;

    static public final String INCREASE_PART_STOCK = """
            UPDATE stan_magazynowy
            SET ilosc = ilosc + :quantity
            WHERE czesci_id = :partId
            """;

    static public final String DELETE_PART_ORDER = """
            DELETE FROM zamowienia
            WHERE id = :partOrderId
            """;

    static public final String DECREASE_PART_STOCK = """
            UPDATE stan_magazynowy
            SET ilosc = ilosc - :quantity
            WHERE czesci_id = :partId
            """;

    static public final String FINALIZE_REPAIR = """
            UPDATE naprawy
            SET koszt = :cost, data_zakonczenia = :endDate
            WHERE id = :repairId
            """;

    static public final String UPDATE_REPAIR_COST_BY_ID = """
            UPDATE naprawy
            SET koszt = :cost
            WHERE id = :id
            """;

    static public final String DELETE_REPAIR_TEMPLATE = """
            DELETE FROM szablony_napraw
            WHERE id = :id
            """;

    static public final String UPDATE_EMPLOYEE_POSITION = """
            UPDATE pracownicy
            SET stanowiska_id = :positionId
            WHERE id = :id
            """;
}
