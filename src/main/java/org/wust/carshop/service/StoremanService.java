package org.wust.carshop.service;

import lombok.AllArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.wust.carshop.exception.ServiceException;
import org.wust.carshop.mapper.PartMapper;
import org.wust.carshop.mapper.PartOrderMapper;
import org.wust.carshop.model.Part;
import org.wust.carshop.model.PartOrder;

import java.util.Iterator;
import java.util.List;

import static org.wust.carshop.query.MechanicQueries.*;
import static org.wust.carshop.query.MechanicQueries.GET_MODEL_ID_BY_NAME;
import static org.wust.carshop.query.StoremanQueries.*;

@AllArgsConstructor
public class StoremanService {
    private final Jdbi dbHandler;


    public Iterator<PartOrder> getAllOrders() {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_ALL_PART_ORDERS)
                .map(new PartOrderMapper())
                .iterator()
        );
    }

    public List<PartOrder> getOrdersByCarAndType(String carModel, String carBrand, String type) {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_PART_ORDERS_BY_CAR_AND_TYPE)
                .bind("type", type)
                .bind("carModel", carModel)
                .bind("carBrand", carBrand)
                .map(new PartOrderMapper())
                .list()
        );
    }

    public List<PartOrder> getXFirstOrders(Integer amount) {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_X_FIRST_PART_ORDERS)
                .bind("amount", amount)
                .map(new PartOrderMapper())
                .list()
        );
    }

    public void deletePartOrder(PartOrder partOrder) {
        var deleted = dbHandler.withHandle(handle -> handle.createUpdate(DELETE_PART_ORDER)
                .bind("partOrderId", partOrder.getId())
                .execute()
        );

        if(deleted != 1) {
            throw new ServiceException("Error while deletign part order with id: " + partOrder.getId());
        }

        var partId = partOrder.getOrderedPart().getId();
        var quantity = partOrder.getQuantity();

        var updated = dbHandler.withHandle(handle -> handle.createUpdate(INCREASE_PART_STOCK)
                .bind("quantity", quantity)
                .bind("partId", partId)
                .execute()
        );

        if(updated != 1) {
            throw new ServiceException("Failed to increase stock of ordered part!");
        }
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

    public int addPartStock(Integer partId, Integer additionalStock) {
        return dbHandler.withHandle(handle -> handle.createUpdate(INCREASE_PART_STOCK)
                .bind("quantity", additionalStock)
                .bind("partId", partId)
                .execute()
        );
    }

    public int addPart(Part part) {
        var producerId = dbHandler.withHandle(handle -> handle.createQuery(GET_PRODUCER_ID_BY_NAME)
                .bind("name", part.getProducer())
                .mapTo(Integer.class)
                .one()
        );

        var typeId = dbHandler.withHandle(handle -> handle.createQuery(GET_TYPE_ID_BY_NAME)
                .bind("name", part.getCategory())
                .mapTo(Integer.class)
                .one()
        );

        var modelId = dbHandler.withHandle(handle -> handle.createQuery(GET_MODEL_ID_BY_NAME)
                .bind("name", part.getCarModel())
                .mapTo(Integer.class)
                .one()
        );

        var created =  dbHandler.withHandle(handle -> handle.createUpdate(INSERT_PART)
                .bind("serialNumber", part.getSerialNumber())
                .bind("cost", part.getPrice())
                .bind("producerId", producerId)
                .bind("typeId", typeId)
                .bind("modelId", modelId)
                .execute()
        );

        if(created != 1) {
            throw new ServiceException("Failed to insert part: " + part);
        }

        return dbHandler.withHandle(handle -> handle.createQuery(GET_HIGHEST_PART_ID)
                .mapTo(Integer.class)
                .one()
        );
    }
}
