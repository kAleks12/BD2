package org.wust.carshop.service;

import lombok.AllArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.wust.carshop.exception.ServiceException;
import org.wust.carshop.mapper.*;
import org.wust.carshop.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.wust.carshop.query.GetQueries.*;
import static org.wust.carshop.query.InsertQueries.*;
import static org.wust.carshop.query.UpdateQueries.*;

@AllArgsConstructor
public class MechanicService {
    private final Jdbi dbHandler;

    public List<String> getCarColors() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CAR_COLORS)
                        .mapTo(String.class)
                        .list()
        );
    }


    public List<String> getCarModels() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CAR_MODELS)
                        .mapTo(String.class)
                        .list()
        );
    }


    public List<String> getCarBrands() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CAR_MANUFACTURERS)
                        .mapTo(String.class)
                        .list()
        );
    }

    public int addCarColor(String name){
        return dbHandler.withHandle(handle -> handle.createUpdate(INSERT_CAR_COLOR)
                .bind("name", name)
                .execute()
        );
    }

    public int addCarBrand(String name) {
        return dbHandler.withHandle(handle -> handle.createUpdate(INSERT_CAR_BRAND)
                .bind("name", name)
                .execute()
        );
    }

    public int addCarModel(String name, Integer brandId) {
        return dbHandler.withHandle(handle -> handle.createUpdate(INSERT_CAR_MODEL)
                .bind("name", name)
                .bind("brand", brandId)
                .execute()
        );
    }

    public boolean carExists(String VIN) {
        return !dbHandler.withHandle(handle ->
                handle.createQuery(GET_CLIENT_ID_BY_VIN)
                        .bind("VIN", VIN)
                        .mapTo(Integer.class)
                        .list()
                        .isEmpty()
        );
    }


    public List<Client> getClientsByFulName(String name, String surname) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_CLIENTS_BY_FULL_NAME)
                        .bind("name", name)
                        .bind("surname", surname)
                        .map(new ClientMapper())
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
                        .mapTo(Integer.class)
                        .one()
        );


        var clientCreated = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_CLIENT)
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
                        .mapTo(Integer.class)
                        .one()
        );

        client.setId(clientId);

        return client;
    }


    public Car createCar(Car car) {
        var owner = car.getOwner();

        if (owner.getId() == null) {
            throw new ServiceException("Cannot INSERT a car with owner that has null id!");
        }

        var colorId = dbHandler.withHandle(handle ->
                handle.createQuery(GET_COLOR_ID_BY_NAME)
                        .bind("name", car.getColor())
                        .mapTo(Integer.class)
                        .one()
        );

        var modelId = dbHandler.withHandle(handle ->
                handle.createQuery(GET_MODEL_ID_BY_NAME)
                        .bind("name", car.getModel())
                        .mapTo(Integer.class)
                        .one()
        );

        var created = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_CAR)
                        .bind("clientId", owner.getId())
                        .bind("colorId", colorId)
                        .bind("modelId", modelId)
                        .bind("VIN", car.getVIN())
                        .bind("productionYear", car.getProductionYear())
                        .execute()
        );

        if (created != 1) {
            throw new ServiceException("Failed to INSERT car :" + car);
        }

        return car;
    }


    public int createRepair(Car car, Integer mechanicId) {
        var created = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_REPAIR)
                        .bind("carVIN", car.getVIN())
                        .bind("employeeId", mechanicId)
                        .bind("startDate", LocalDate.now())
                        .execute()
        );

        if (created != 1) {
            throw new ServiceException("Failed to INSERT repair for car :" + car);
        }

        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_MAX_REPAIRS_ID)
                        .mapTo(Integer.class)
                        .one()
        );
    }


    public List<Repair> getActiveRepairs() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_ACTIVE_REPAIRS)
                        .map(new RepairMapper())
                        .list()
        );
    }

    public Iterator<Part> getAllParts() {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_ALL_PARTS)
                        .map(new PartMapper())
                        .iterator()
        );
    }

    public Iterator<Part> getPartsByFullFilter(String carModel, String carBrand,
                                               String manufacturer, String type) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_PARTS_BY_MANUFACTURER_AND_TYPE_AND_CAR)
                        .bind("type", type)
                        .bind("carModel", carModel)
                        .bind("carBrand", carBrand)
                        .bind("manufacturer", manufacturer)
                        .map(new PartMapper())
                        .iterator()
        );
    }

    public Iterator<Part> getPartsByCar(String carModel, String carBrand) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_PARTS_BY_CAR)
                        .bind("carModel", carModel)
                        .bind("carBrand", carBrand)
                        .map(new PartMapper())
                        .iterator()
        );
    }

    public Iterator<Part> getPartsByCarAndManufacturer(String carModel,
                                                       String carBrand, String manufacturer) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_PARTS_BY_MANUFACTURER_AND_CAR)
                        .bind("carBrand", carBrand)
                        .bind("carModel", carModel)
                        .bind("manufacturer", manufacturer)
                        .map(new PartMapper())
                        .iterator()
        );
    }

    public Iterator<Part> getPartsByCarAndType(String carModel,
                                               String carBrand, String type) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_PARTS_BY_TYPE_AND_CAR)
                        .bind("carBrand", carBrand)
                        .bind("carModel", carModel)
                        .bind("type", type)
                        .map(new PartMapper())
                        .iterator()
        );
    }

    public void addPartToRepair(Part usedPart, Integer quantity, Integer repairId) {
        var stock = getPartsStock(usedPart.getId());

        if (stock < quantity) {
            var errorMsg = "Cannot add part because stock is: %d and quantity is: %d";
            throw new ServiceException(errorMsg.formatted(stock, quantity));
        }

        var updated = dbHandler.withHandle(handle ->
                handle.createUpdate(DECREASE_PART_STOCK)
                        .bind("partId", usedPart.getId())
                        .bind("quantity", quantity)
                        .execute()
        );

        if (updated != 1) {
            throw new ServiceException("Failed to UPDATE part's stock: " + usedPart);
        }

        var created = dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_USED_PART)
                        .bind("partId", usedPart.getId())
                        .bind("quantity", quantity)
                        .bind("repairId", repairId)
                        .execute()
        );

        if (created != 1) {
            throw new ServiceException("Failed to INSERT used part: " + usedPart);
        }

    }

    public int finalizeRepair(Integer repairId) {
        var repairCost = dbHandler.withHandle(handle ->
                handle.createQuery(GET_REPAIR_COST)
                        .bind("repairId", repairId)
                        .mapTo(Double.class)
                        .one()
        );

        return dbHandler.withHandle(handle ->
                handle.createUpdate(FINALIZE_REPAIR)
                        .bind("cost", repairCost)
                        .bind("endDate", LocalDate.now())
                        .bind("repairId", repairId)
                        .execute()
        );
    }

    public int createPartOrder(Part part, Integer quantity, Integer mechanicId) {
        if (part.getId() == null) {
            throw new ServiceException("Cannot order a part which has null id!");
        }

        return dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_PART_ORDER)
                        .bind("quantity", quantity)
                        .bind("cost", quantity * part.getPrice())
                        .bind("date", LocalDate.now())
                        .bind("employeeId", mechanicId)
                        .bind("partId", part.getId())
                        .execute()
        );
    }


    public List<RepairTemplate> getAllRepairTemplates() {
        var templateSignatures = dbHandler.withHandle(handle ->
                handle.createQuery(GET_ALL_TEMPLATES)
                        .map(new RepairTemplateMapper())
                        .list()
        );
        List<RepairTemplate> fullTemplates = new ArrayList<>();

        templateSignatures.forEach(template -> {
            var parts = dbHandler.withHandle(handle -> handle.createQuery(GET_PARTS_FOR_TEMPLATE)
                    .bind("templateId", template.getId())
                    .map(new PartPairMapper())
                    .list()
            );

            template.setRequiredParts(parts);
            fullTemplates.add(template);
        });

        return fullTemplates;
    }

    public RepairTemplate getRepairTemplateByName(String name) {
        var template = dbHandler.withHandle(handle ->
                handle.createQuery(GET_TEMPLATE_BY_NAME)
                        .bind("name", name)
                        .map(new RepairTemplateMapper())
                        .one()
        );


        var parts = dbHandler.withHandle(handle -> handle.createQuery(GET_PARTS_FOR_TEMPLATE)
                .bind("templateId", template.getId())
                .map(new PartPairMapper())
                .list()
        );

        template.setRequiredParts(parts);


        return template;
    }

    public int createRepairFromTemplate(RepairTemplate template, Car car, Integer mechanicId) {
        checkRequiredParts(template);

        var id = createRepair(car, mechanicId);

        var updated = dbHandler.withHandle(handle ->
                handle.createUpdate(UPDATE_REPAIR_COST_BY_ID)
                        .bind("id", id)
                        .bind("cost", template.getCost())
                        .execute()
        );

        if (updated != 1) {
            throw new ServiceException("Failed to update cost in createRepairFromTemplate");
        }

        template.getRequiredParts().forEach(pair ->
                addPartToRepair(pair.getPart(), pair.getQuantity(), id)
        );

        return id;
    }


    private int createAddress(Address address) {
        return dbHandler.withHandle(handle ->
                handle.createUpdate(INSERT_ADDRESS)
                        .bind("city", address.getCity())
                        .bind("postalCode", address.getPostalCode())
                        .bind("building", address.getBuildingNumber())
                        .bind("apartment", address.getApartment())
                        .bind("street", address.getStreet())
                        .execute()
        );

    }

    private int getPartsStock(Integer partId) {
        return dbHandler.withHandle(handle ->
                handle.createQuery(GET_PART_STOCK_BY_ID)
                        .bind("partId", partId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    private void checkRequiredParts(RepairTemplate template) {
        template.getRequiredParts().forEach(pair -> {
            var quantity = pair.getQuantity();
            var part = pair.getPart();
            var stock = getPartsStock(part.getId());

            if (quantity > stock) {
                var errorMsg = """
                                Cannot add repair from template because stock for part: %s
                                is %d and required quantity is %d
                        """;
                throw new ServiceException(errorMsg.formatted(part, stock, quantity));
            }
        });
    }
}