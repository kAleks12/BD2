package org.wust.carshop.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jdbi.v3.core.Jdbi;
import org.wust.carshop.TableConfig;
import org.wust.carshop.exception.ServiceException;
import org.wust.carshop.model.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
public class MechanicService {
    private final Jdbi dbHandler;
    private final Integer mechanicId;
    private TableConfig tables = TableConfig.builder().build();

    private final String CHECK_CAR_BY_VIN = "SELECT id from :carTable WHERE VIN = ':VIN'";
    private final String GET_CLIENTS_BY_FULL_NAME = """
            SELECT c.imie,
            c.nazwisko,
            a.miasto,
            a.kod_pocztowy,
            a.ulica,
            a.numer_budynku,
            a.numer_mieszkania
            FROM :clientsTable c
            JOIN :addressesTable a ON c.adres_id = a.id
            WHERE c.nazwisko = ':surname' and c.imie = ':name'
            """;

    private final String GET_ACTIVE_REPAIRS = """
            SELECT e.imie || ' ' || e.nazwisko as mechanic,
                   p.nazwa as position,
                   cars.VIN ,
                   cars.rok_produkcji,
                   models.nazwa as model,
                   m.nazwa as manufacturer,
                   c.nazwa as color,
                   r.data_rozpoczecia as start_date
            FROM :repairsTable r
            JOIN :employeesTable e ON r.pracownicy_id = e.id
            JOIN :positionsTable p ON e.stanowiska_id = p.id
            JOIN :carsTable cars ON r.samochody_VIN = cars.VIN
            JOIN :colorsTable c ON cars.kolory_id = c.id
            JOIN :modelsTable models ON cars.modele_id = models.id
            JOIN :manufacturersTable m ON models.marki_id = m.id
            WHERE r.data_zakonczenia IS NULL or r.koszt IS NULL
            """;

    private final String GET_COLOR_ID_BY_NAME = "SELECT id FROM :colorsTable WHERE nazwa = ':name'";
    private final String GET_MODEL_ID_BY_NAME = "SELECT id FROM :modelsTable WHERE nazwa = ':name'";

    private final String GET_CAR_COLORS = "SELECT nazwa from :colorsTable";
    private final String GET_CAR_MODELS = "SELECT nazwa from :modelsTable";
    private final String GET_CAR_MANUFACTURERS = "SELECT nazwa from :manufacturersTable";
    private final String GET_MAX_ADDRESS_ID = "SELECT MAX(id) FROM :addressesTable";
    private final String GET_MAX_CLIENTS_ID = "SELECT MAX(id) FROM :clientsTable";
    private final String GET_MAX_CARS_ID = "SELECT MAX(id) FROM :carsTable";
    private final String GET_MAX_REPAIRS_ID = "SELECT MAX(id) FROM :repairsTable";
    private final String INSERT_CLIENT = """
            INSERT INTO :clientsTable(imie, nazwisko, adres_id)
            VALUES(:name, :surname, :addressId)
            """;
    private final String INSERT_ADDRESS = """
            INSERT INTO :addressesTable(miasto, kod_pocztowy, ulica, numer_budynku, numer_mieszkania)
            VALUES(:city, :postalCode, :building, :apartment)
            """;

    private final String INSERT_CAR = """
            INSERT INTO :carsTable(klienci_id, kolory_id, modele_id, VIN, rok_produkcji)
            VALUES(:clientId, :colorId, :modelId, :VIN, :productionYear)
            """;

    private final String INSERT_REPAIR = """
            INSERT INTO :repairsTable(samochody_VIN, pracownicy_id, data_rozpoczecia)
            VALUES(:carVIN, :employeeId, :startDate)
            """;
    private final String INSERT_USED_PART = """
            INSERT INTO :usedPartsTable(naprawy_id, czesci_id, ilosc)
            VALUES(:repairId, :partId, :quantity)
            """;

    private final String DECREASE_PART_STOCK = """
            UPDATE :stockTable
            SET ilosc = ilosc - :quantity
            WHERE czesci_id = :partId
            """;

    public List<String> getCarColors() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CAR_COLORS)
                        .bind("colorsTable", tables.getCarColorsTable())
                        .mapTo(String.class)
                        .list()
        );
    }

    public List<String> getCarModels() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CAR_MODELS)
                        .bind("modelsTable", tables.getCarModelsTable())
                        .mapTo(String.class)
                        .list()
        );
    }

    public List<String> getCarManufacturers() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CAR_MANUFACTURERS)
                        .bind("manufacturersTable", tables.getCarManufacturersTable())
                        .mapTo(String.class)
                        .list()
        );
    }

    public boolean carExists(String VIN) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(CHECK_CAR_BY_VIN)
                        .bind("carTable", tables.getCarsTable())
                        .bind("VIN", VIN)
                        .mapTo(Integer.class)
                        .list()
                        .isEmpty()
        );
    }

    public List<Client> getClientsByFulName(String name, String surname) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CLIENTS_BY_FULL_NAME)
                        .bind("clientsTable", tables.getClientsTable())
                        .bind("addressesTable", tables.getAddressesTable())
                        .bind("name", name)
                        .bind("surname", surname)
                        .mapTo(Client.class)
                        .list()
        );
    }

    public Client createClient(Client client) {
        var address = client.getAddress();
        var addressCreated = createAddress(address);

        if (addressCreated != 1) {
            throw new ServiceException("Failed to INSERT address: " + address);
        }

        var addressId = dbHandler.withHandle(handle ->
                handle.createQuery(GET_MAX_ADDRESS_ID)
                        .bind("addressesTable", tables.getAddressesTable())
                        .mapTo(Integer.class)
        );


        var clientCreated = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_CLIENT)
                        .bind("clientsTable", tables.getClientsTable())
                        .bind("name", client.getName())
                        .bind("surname", client.getSurname())
                        .bind("addressId", addressId)
                        .execute()
        );

        if (clientCreated != 1) {
            throw new ServiceException("Failed to INSERT client: " + client);
        }

        var clientId = dbHandler.withHandle(handle ->
                handle.createQuery(GET_MAX_CLIENTS_ID)
                        .bind("clientsTable", tables.getClientsTable())
                        .mapTo(Integer.class)
                        .one()
        );

        client.setId(clientId);

        return client;
    }

    public Car createCar(Car car, Client client) {
        if (client.getId() == null) {
            throw new ServiceException("Cannot INSERT a car with owner that has null id!");
        }

        var colorId = dbHandler.withHandle(handle ->
                handle.createQuery(GET_COLOR_ID_BY_NAME)
                        .bind("colorsTable", tables.getCarColorsTable())
                        .bind("name", car.getColor())
                        .mapTo(Integer.class)
                        .one()
        );

        var modelId = dbHandler.withHandle(handle ->
                handle.createQuery(GET_MODEL_ID_BY_NAME)
                        .bind("modelsTable", tables.getCarModelsTable())
                        .bind("name", car.getModel())
                        .mapTo(Integer.class)
                        .one()
        );

        var created = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_CAR)
                        .bind("carsTable", tables.getCarsTable())
                        .bind("clientId", client.getId())
                        .bind("colorId", colorId)
                        .bind("modelId", modelId)
                        .bind("VIN", car.getVIN())
                        .bind("productionYear", car.getProductionYear())
                        .execute()
        );

        if (created != 1) {
            throw new ServiceException("Failed to INSERT car :" + car);
        }

        var carId = dbHandler.withHandle(handle ->
                handle.createQuery(GET_MAX_CARS_ID)
                        .bind("carsTable", tables.getCarsTable())
                        .mapTo(Integer.class)
                        .one()
        );

        car.setId(carId);

        return car;
    }

    public int createRepair(Car car) {
        if (car.getId() == null) {
            throw new ServiceException("Cannot repair a car which has null id!");
        }

        var created = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_REPAIR)
                        .bind("repairsTable", tables.getRepairsTable())
                        .bind("carVIN", car.getVIN())
                        .bind("employeeId", mechanicId)
                        .bind("startDate", LocalDate.now().toString())
                        .execute()
        );

        if (created != 1) {
            throw new ServiceException("Failed to INSERT repair for car :" + car);
        }

        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_MAX_REPAIRS_ID)
                        .bind("repairsTable", tables.getRepairsTable())
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<Repair> getActiveRepairs() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_ACTIVE_REPAIRS)
                        .bind("repairsTable", tables.getRepairsTable())
                        .bind("employeesTable", tables.getEmployeesTable())
                        .bind("positionsTable", tables.getEmployeePositionsTable())
                        .bind("carsTable", tables.getCarsTable())
                        .bind("modelsTable", tables.getCarModelsTable())
                        .bind("manufacturersTable", tables.getCarManufacturersTable())
                        .bind("colorsTable", tables.getCarColorsTable())
                        .mapTo(Repair.class)
                        .list()
        );
    }

    public void addPartToRepair(Part usedPart, Integer quantity, Integer repairId) {
        var created = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_USED_PART)
                        .bind("usedPartsTable", tables.getRepairUsedPartsTable())
                        .bind("partId", usedPart.getId())
                        .bind("quantity", quantity)
                        .bind("repairId", repairId)
                        .execute()
        );

        if (created != 1) {
            throw new ServiceException("Failed to INSERT used part: " + usedPart);
        }

        var updated = dbHandler.withHandle(handle ->
                handle.createUpdate(DECREASE_PART_STOCK)
                        .bind("stockTable", tables.getPartStockTable())
                        .bind("partId", usedPart.getId())
                        .bind("quantity", quantity)
                        .execute()
        );

        if (updated != 1) {
            throw new ServiceException("Failed to INSERT used part: " + usedPart);
        }
    }

    private int createAddress(Address address) {
        return dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_ADDRESS)
                        .bind("addressesTable", tables.getAddressesTable())
                        .bind("city", address.getCity())
                        .bind("postalCode", address.getPostalCode())
                        .bind("building", address.getBuildingNumber())
                        .bind("apartment", address.getApartment())
                        .execute()
        );

    }

    //TODO implement finalizing repairs
    //TODO implement searching through parts
    //TODO implement adding orders for parts
    //TODO extract queries to another class
}
