package org.wust.carshop.query;

public class MechanicQueries {
    static public final String CHECK_CAR_BY_VIN = "SELECT klienci_id FROM samochody WHERE VIN = :VIN";
    static public final String GET_CLIENTS_BY_FULL_NAME = """
            SELECT *
            FROM klienci c
            JOIN adres a ON c.adres_id = a.id
            WHERE c.nazwisko = :surname and c.imie = :name
            """;

    static public final String GET_ACTIVE_REPAIRS = """
            SELECT e.id,
                   e.imie as p_name,
                   e.nazwisko as p_surname,
                   p.nazwa as p_position,
                   cars.VIN ,
                   cars.rok_produkcji as production_year,
                   models.nazwa as model,
                   c.nazwa as color,
                   r.data_rozpoczecia as start_date,
                   k.*,
                   a.*
            FROM naprawy r
            JOIN pracownicy e ON r.pracownicy_id = e.id
            JOIN stanowiska p ON e.stanowiska_id = p.id
            JOIN samochody cars ON r.samochody_VIN = cars.VIN
            JOIN kolory c ON cars.kolory_id = c.id
            JOIN modele models ON cars.modele_id = models.id
            JOIN klienci k ON cars.klienci_id = k.id
            JOIN adres a ON k.adres_id = a.id
            WHERE r.data_zakonczenia IS NULL or r.koszt IS NULL
            """;

    static public final String GET_COLOR_ID_BY_NAME = "SELECT id FROM kolory WHERE nazwa = :name";
    static public final String GET_MODEL_ID_BY_NAME = "SELECT id FROM modele WHERE nazwa = :name";

    static public final String GET_CAR_COLORS = "SELECT nazwa from kolory";
    static public final String GET_CAR_MODELS = "SELECT nazwa from modele";
    static public final String GET_CAR_MANUFACTURERS = "SELECT nazwa from marki";
    static public final String GET_MAX_ADDRESS_ID = "SELECT MAX(id) FROM adres";
    static public final String GET_MAX_CLIENTS_ID = "SELECT MAX(id) FROM klienci";
    static public final String GET_MAX_REPAIRS_ID = "SELECT MAX(id) FROM naprawy";
    static public final String GET_PART_STOCK_BY_ID = "SELECT ilosc FROM stan_magazynowy WHERE czesci_id = :partId";

    static public final String INSERT_CLIENT = """
            INSERT INTO klienci(imie, nazwisko, adres_id)
            VALUES(:name, :surname, :addressId)
            """;
    static public final String INSERT_ADDRESS = """
            INSERT INTO adres(miasto, kod_pocztowy, ulica, numer_budynku, numer_mieszkania)
            VALUES(:city, :postalCode, :street,  :building, :apartment)
            """;

    static public final String INSERT_CAR = """
            INSERT INTO samochody(klienci_id, kolory_id, modele_id, VIN, rok_produkcji)
            VALUES(:clientId, :colorId, :modelId, :VIN, :productionYear)
            """;

    static public final String INSERT_REPAIR = """
            INSERT INTO naprawy(samochody_VIN, pracownicy_id, data_rozpoczecia)
            VALUES(:carVIN, :employeeId, :startDate)
            """;
    static public final String INSERT_USED_PART = """
            INSERT INTO uzyte_czesci(naprawy_id, czesci_id, ilosc)
            VALUES(:repairId, :partId, :quantity)
            """;

    static public final String INSERT_PART_ORDER = """
            INSERT INTO zamowienia(ilosc, cena, data, pracownicy_id, czesci_id)
            VALUES(:quantity, :cost, :date, :employeeId, :partId)
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

    static public final String GET_REPAIR_COST = """
            SELECT SUM(p.koszt * u.ilosc)
            FROM uzyte_czesci u
            JOIN czesci p ON  p.id = u.czesci_id
            WHERE u.naprawy_id = :repairId
            """;

    static public final String GET_ALL_PARTS = """
            SELECT p.id,
            p.numer_seryjny as serial_number,
            p.koszt as price,
            cm.nazwa as car_model,
            pt.nazwa as type,
            pm.nazwa as producer,
            m.nazwa as car_brand
            FROM czesci p
            JOIN typy pt ON p.typy_id = pt.id
            JOIN modele cm ON cm.id = p.modele_id
            JOIN producenci pm ON pm.id = p.producenci_id
            JOIN marki m ON m.id = cm.marki_id
            """;

    static public final String GET_ALL_TEMPLATES = """
            SELECT *
            FROM szablony_napraw
            """;

    static public final String GET_TEMPLATE_BY_NAME = """
            SELECT *
            FROM szablony_napraw
            WHERE nazwa = :name
            """;

    static public final String GET_PARTS_FOR_TEMPLATE = """
            SELECT wc.ilosc as quantity,
             p.id,
             p.numer_seryjny as serial_number,
             p.koszt as price,
             cm.nazwa as car_model,
             pt.nazwa as type,
             pm.nazwa as producer,
             m.nazwa as car_brand
             FROM wymagane_czesci wc
             JOIN czesci p ON p.id = wc.czesci_id
             JOIN typy pt ON p.typy_id = pt.id
             JOIN modele cm ON cm.id = p.modele_id
             JOIN producenci pm ON pm.id = p.producenci_id
             JOIN marki m ON m.id = cm.marki_id
             WHERE wc.szablony_napraw_id = :templateId
            """;

    static public final String UPDATE_REPAIR_COST_BY_ID = """
            UPDATE naprawy
            SET koszt = :cost
            WHERE id = :id
            """;

    static public final String GET_PARTS_BY_TYPE_AND_CAR = """
            SELECT p.id,
             p.numer_seryjny as serial_number,
             p.koszt as price,
             cm.nazwa as car_model,
             pt.nazwa as type,
             pm.nazwa as producer,
             m.nazwa as car_brand
             FROM czesci p
             JOIN typy pt ON p.typy_id = pt.id
             JOIN modele cm ON cm.id = p.modele_id
             JOIN producenci pm ON pm.id = p.producenci_id
             JOIN marki m ON m.id = cm.marki_id
             WHERE pt.nazwa = :type and cm.nazwa = :carModel and m.nazwa = :carBrand
              """;

    static public final String GET_PARTS_BY_MANUFACTURER_AND_CAR = """
            SELECT p.id,
            p.numer_seryjny as serial_number,
            p.koszt as price,
            cm.nazwa as car_model,
            pt.nazwa as type,
            pm.nazwa as producer,
            m.nazwa as car_brand
            FROM czesci p
            JOIN typy pt ON p.typy_id = pt.id
            JOIN modele cm ON cm.id = p.modele_id
            JOIN producenci pm ON pm.id = p.producenci_id
            JOIN marki m ON m.id = cm.marki_id
             WHERE pm.nazwa = :manufacturer and cm.nazwa = :carModel and m.nazwa = :carBrand
              """;

    static public final String GET_PARTS_BY_MANUFACTURER_AND_TYPE_AND_CAR = """
            SELECT p.id,
             p.numer_seryjny as serial_number,
             p.koszt as price,
             cm.nazwa as car_model,
             pt.nazwa as type,
             pm.nazwa as producer,
             m.nazwa as car_brand
             FROM czesci p
             JOIN typy pt ON p.typy_id = pt.id
             JOIN modele cm ON cm.id = p.modele_id
             JOIN producenci pm ON pm.id = p.producenci_id
             JOIN marki m ON m.id = cm.marki_id
             WHERE pt.nazwa = :type AND cm.nazwa = :carModel
             AND pm.nazwa = :manufacturer and m.nazwa = :carBrand
              """;

    static public final String GET_PARTS_BY_CAR = """
            SELECT p.id,
             p.numer_seryjny as serial_number,
             p.koszt as price,
             cm.nazwa as car_model,
             pt.nazwa as type,
             pm.nazwa as producer,
             m.nazwa as car_brand
             FROM czesci p
             JOIN typy pt ON p.typy_id = pt.id
             JOIN modele cm ON cm.id = p.modele_id
             JOIN producenci pm ON pm.id = p.producenci_id
             JOIN marki m ON m.id = cm.marki_id
             WHERE cm.nazwa = :carModel and m.nazwa = :carBrand
              """;
}
