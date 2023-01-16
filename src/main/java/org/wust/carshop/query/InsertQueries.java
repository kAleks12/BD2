package org.wust.carshop.query;

public class InsertQueries {
    static public final String INSERT_REPAIR_TEMPLATE = """
            INSERT INTO szablony_napraw(nazwa, koszt)
            VALUES(:name, :cost)
            """;

    static public final String INSERT_REQUIRED_PART = """
            INSERT INTO wymagane_czesci(czesci_id, szblony_napraw_id, ilosc)
            VALUES(:partId, :templateId, :quantity)
            """;

    static public final String INSERT_EMPLOYEE = """
            INSERT INTO pracownicy(imie, nazwisko, stanowiska_id, haslo)
            VALUES(:name, :surname, :positionId, :password)
            """;

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

    static public final String INSERT_PART = """
           INSERT INTO czesci(numer_seryjny, producenci_id, typy_id, modele_id, koszt)
           VALUES(:serialNumber, :producerId, :typeId, :modelId, :cost)
            """;

    static public final String INSERT_PART_TYPE = """
           INSERT INTO typy(nazwa)
           VALUES(:name)
            """;

    static public final String INSERT_PART_PRODUCER = """
           INSERT INTO producenci(nazwa)
           VALUES(:name)
            """;

    static public final String INSERT_CAR_COLOR = """
           INSERT INTO kolory(nazwa)
           VALUES(:name)
            """;

    static public final String INSERT_CAR_MODEL = """
           INSERT INTO modele(nazwa, marki_id)
           VALUES(:name, :brandId)
            """;

    static public final String INSERT_CAR_BRAND = """
           INSERT INTO marki(nazwa)
           VALUES(:name)
            """;

    static public final String INSERT_POSITION = """
           INSERT INTO stanowiska(nazwa)
           VALUES(:name)
            """;
}
