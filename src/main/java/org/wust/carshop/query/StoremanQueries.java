package org.wust.carshop.query;

public class StoremanQueries {
    public static final String GET_ALL_PART_ORDERS = """
            SELECT z.id as z_id,
                   z.ilosc,
                   z.cena,
                   z.data,
                   p.id as p_id,
                   p.imie,
                   p.nazwisko,
                   s.nazwa as position,
                   c.id,
                   c.numer_seryjny as serial_number,
                   c.koszt as price,
                   m.nazwa as car_model,
                   mar.nazwa as car_brand,
                   pro.nazwa as producer,
                   t.nazwa as type
            FROM zamowienia z
            JOIN czesci c ON c.id = z.czesci_id
            JOIN pracownicy p ON p.id = z.pracownicy_id
            JOIN stanowiska s ON s.id = p.stanowiska_id
            JOIN modele m ON c.modele_id = m.id
            JOIN marki mar ON mar.id = m.marki_id
            JOIN producenci pro ON pro.id = c.producenci_id
            JOIN typy t ON t.id = c.typy_id
            ORDER BY z.id
            """;

    public static final String GET_PART_ORDERS_BY_CAR_AND_TYPE = """
            SELECT z.id as z_id,
                   z.ilosc,
                   z.cena,
                   z.data,
                   p.id as p_id,
                   p.imie,
                   p.nazwisko,
                   s.nazwa as position,
                   c.id,
                   c.numer_seryjny as serial_number,
                   c.koszt as price,
                   m.nazwa as car_model,
                   mar.nazwa as car_brand,
                   pro.nazwa as producer,
                   t.nazwa as type
            FROM zamowienia z
            JOIN czesci c ON c.id = z.czesci_id
            JOIN pracownicy p ON p.id = z.pracownicy_id
            JOIN stanowiska s ON s.id = p.stanowiska_id
            JOIN modele m ON c.modele_id = m.id
            JOIN marki mar ON mar.id = m.marki_id
            JOIN producenci pro ON pro.id = c.producenci_id
            JOIN typy t ON t.id = c.typy_id
            WHERE t.nazwa = :type AND m.nazwa = :carModel AND mar.nazwa = :carBrand
            """;

    public static final String GET_X_FIRST_PART_ORDERS = """
            SELECT z.id as z_id,
                   z.ilosc,
                   z.cena,
                   z.data,
                   p.id as p_id,
                   p.imie,
                   p.nazwisko,
                   s.nazwa as position,
                   c.id,
                   c.numer_seryjny as serial_number,
                   c.koszt as price,
                   m.nazwa as car_model,
                   mar.nazwa as car_brand,
                   pro.nazwa as producer,
                   t.nazwa as type
            FROM zamowienia z
            JOIN czesci c ON c.id = z.czesci_id
            JOIN pracownicy p ON p.id = z.pracownicy_id
            JOIN stanowiska s ON s.id = p.stanowiska_id
            JOIN modele m ON c.modele_id = m.id
            JOIN marki mar ON mar.id = m.marki_id
            JOIN producenci pro ON pro.id = c.producenci_id
            JOIN typy t ON t.id = c.typy_id
            ORDER BY z.id
            LIMIT :amount
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

    static public final String GET_PRODUCER_ID_BY_NAME = """
            SELECT id
            FROM producenci
            WHERE nazwa = :name
            """;

    static public final String GET_TYPE_ID_BY_NAME = """
            SELECT id
            FROM typy
            WHERE nazwa = :name
            """;

    static public final String INSERT_PART = """
           INSERT INTO czesci(numer_seryjny, producenci_id, typy_id, modele_id, koszt)
           VALUES(:serialNumber, :producerId, :typeId, :modelId, :cost)
            """;

    static public final String GET_HIGHEST_PART_ID = """
           SELECT MAX(id) FROM czesci
            """;
}