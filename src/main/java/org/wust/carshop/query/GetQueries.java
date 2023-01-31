package org.wust.carshop.query;

public class GetQueries {
    static public final String GET_MAX_REPAIR_TEMPLATE_ID = """
            SELECT MAX(id) FROM szablony_napraw
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

    static public final String GET_HIGHEST_PART_ID = """
            SELECT MAX(id) FROM czesci
             """;

    static public final String GET_MODEL_BY_BRAND = """
            SELECT modele.nazwa
            FROM modele
            JOIN marki on marki.id = modele.marki_id
            WHERE marki.nazwa = :name
            """;

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

    static public final String GET_FILTERED_TEMPLATES = """
            SELECT *
            FROM szablony_napraw
            WHERE nazwa like :arg
            """;

    static public final String GET_TEMPLATE_BY_NAME = """
            SELECT *
            FROM szablony_napraw
            WHERE nazwa = :name
            """;

    static public final String GET_ADDRESS_ID = """
            SELECT id
            FROM adres
            WHERE miasto = :city
            AND ulica = :street
            AND numer_budynku = :building
            AND numer_mieszkania = :apartment
            AND kod_pocztowy = :zipCode
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
                   brands.nazwa as brand,
                   c.nazwa as color,
                   r.data_rozpoczecia as start_date,
                   r.id as r_id,
                   k.*,
                   a.*
            FROM naprawy r
            JOIN pracownicy e ON r.pracownicy_id = e.id
            JOIN stanowiska p ON e.stanowiska_id = p.id
            JOIN samochody cars ON r.samochody_VIN = cars.VIN
            JOIN kolory c ON cars.kolory_id = c.id
            JOIN modele models ON cars.modele_id = models.id
            JOIN marki brands ON models.marki_id = brands.id
            JOIN klienci k ON cars.klienci_id = k.id
            JOIN adres a ON k.adres_id = a.id
            WHERE r.data_zakonczenia IS NULL or r.koszt IS NULL
            """;

    static public final String GET_ALL_EMPLOYEES = """
            SELECT e.*,
            s.nazwa
            FROM pracownicy e
            JOIN stanowiska s on s.id = e.stanowiska_id
            """;

    static public final String GET_EMPLOYEES_BY_FULL_NAME = """
            SELECT e.*,
            s.nazwa
            FROM pracownicy e
            JOIN stanowiska s on s.id = e.stanowiska_id
            WHERE imie = :name AND nazwisko = :surname
            """;

    static public final String GET_EMPLOYEES_BY_FULL_NAME_AND_POSITION = """
            SELECT e.*,
            s.nazwa
            FROM pracownicy e
            JOIN stanowiska s on s.id = e.stanowiska_id
            WHERE imie = :name AND nazwisko = :surname AND s.nazwa = :position
            """;
    static public final String GET_EMPLOYEES_BY_POSITION = """
            SELECT e.*,
            s.nazwa
            FROM pracownicy e
            JOIN stanowiska s on s.id = e.stanowiska_id
            WHERE s.nazwa = :position
            """;


    static public final String GET_COLOR_ID_BY_NAME = "SELECT id FROM kolory WHERE nazwa = :name";
    static public final String GET_MODEL_ID_BY_NAME = "SELECT id FROM modele WHERE nazwa = :name";
    static public final String GET_BRAND_ID_BY_NAME = "SELECT id FROM marki WHERE nazwa = :name";
    static public final String GET_CAR_COLORS = "SELECT nazwa FROM kolory";
    static public final String GET_CAR_MODELS = "SELECT nazwa FROM modele";
    static public final String GET_POSITIONS = "SELECT nazwa FROM stanowiska";
    static public final String GET_PART_TYPES = "SELECT nazwa FROM typy";
    static public final String GET_PART_PRODUCERS = "SELECT nazwa FROM producenci";
    static public final String GET_CAR_MANUFACTURERS = "SELECT nazwa FROM marki";
    static public final String GET_MAX_ADDRESS_ID = "SELECT MAX(id) FROM adres";
    static public final String GET_MAX_CLIENTS_ID = "SELECT MAX(id) FROM klienci";
    static public final String GET_MAX_REPAIRS_ID = "SELECT MAX(id) FROM naprawy";
    static public final String GET_PART_STOCK_BY_ID = "SELECT ilosc FROM stan_magazynowy WHERE czesci_id = :partId";
    static public final String GET_CLIENT_ID_BY_VIN = "SELECT klienci_id FROM samochody WHERE VIN = :VIN";
    static public final String GET_POSITION_ID_BY_NAME = "SELECT id FROM stanowiska WHERE nazwa = :name";
    static public final String BRAND_EXISTS = "SELECT COUNT(id) FROM marki WHERE nazwa = :name";
    static public final String MODEL_EXISTS = "SELECT COUNT(id) FROM modele WHERE nazwa = :name";
    static public final String COLOR_EXISTS = "SELECT COUNT(id) FROM kolory WHERE nazwa = :name";
    static public final String PART_TYPE_EXISTS = "SELECT COUNT(id) FROM typy WHERE nazwa = :name";
    static public final String PART_PRODUCER_EXISTS = "SELECT COUNT(id) FROM producenci WHERE nazwa = :name";
    static public final String POSITION_EXISTS = "SELECT COUNT(id) FROM stanowiska WHERE nazwa = :name";
    static public final String TEMPLATE_EXISTS = "SELECT COUNT(id) FROM szablony_napraw WHERE nazwa = :name";
    static public final String ADDRESS_EXISTS = """
            SELECT COUNT(id)
            FROM adres
            WHERE miasto = :city AND kod_pocztowy = :postalCode AND ulica = :street
            AND numer_budynku = :building AND numer_mieszkania = :apartment
            """;

    static public final String PART_EXISTS = """
            SELECT COUNT(id)
            FROM czesci
            WHERE numer_seryjny = :serialNumber
            """;

    static public final String VALIDATE = """
            SELECT id
            FROM pracownicy
            WHERE haslo = :password AND login = :login
            """;

    static public final String GET_PASSWORD = """
           SELECT rolpassword FROM pg_authid WHERE rolname = :role
            """;

    static public final String GET_ROLE_BY_ID = """
           SELECT s.nazwa
           FROM pracownicy p
           JOIN stanowiska s ON s.id = p.stanowiska_id
            """;

}
