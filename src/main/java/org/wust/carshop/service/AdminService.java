package org.wust.carshop.service;

import lombok.AllArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.wust.carshop.exception.ServiceException;
import org.wust.carshop.mapper.EmployeeMapper;
import org.wust.carshop.model.Employee;
import org.wust.carshop.model.Part;
import org.wust.carshop.model.RepairTemplate;
import org.wust.carshop.util.PartPair;

import java.util.Iterator;
import java.util.List;

import static org.wust.carshop.query.GetQueries.*;
import static org.wust.carshop.query.InsertQueries.*;
import static org.wust.carshop.query.UpdateQueries.*;

@AllArgsConstructor
public class AdminService {
    private final Jdbi dbHandler;
    private final UtilsService us;

    public Iterator<Part> getAllParts() {
        return us.getAllParts();
    }

    public Iterator<Part> getPartsByFullFilter(String carModel, String carBrand,
                                               String manufacturer, String type) {
        return us.getPartsByFullFilter(carModel, carBrand, manufacturer, type);
    }

    public Iterator<Part> getPartsByCar(String carModel, String carBrand) {
        return us.getPartsByCar(carModel, carBrand);
    }

    public Iterator<Part> getPartsByCarAndManufacturer(String carModel,
                                                       String carBrand, String manufacturer) {
        return us.getPartsByCarAndManufacturer(carModel, carBrand, manufacturer);
    }

    public Iterator<Part> getPartsByCarAndType(String carModel,
                                               String carBrand, String type) {
        return us.getPartsByCarAndType(carModel, carBrand, type);
    }

    public List<RepairTemplate> getAllRepairTemplates() {
        return us.getAllRepairTemplates();
    }

    public RepairTemplate getRepairTemplateByName(String name) {
        return us.getRepairTemplateByName(name);
    }

    public void addRepairTemplate(String name, List<PartPair> requiredParts) {
        double currCost = 0;
        for (var pair : requiredParts) {
            currCost += pair.getQuantity() * pair.getPart().getPrice();
        }

        final double finalCost = currCost;

        var created = dbHandler.withHandle(handle -> handle.createUpdate(INSERT_REPAIR_TEMPLATE)
                .bind("name", name)
                .bind("cost", finalCost)
                .execute()
        );

        var templateId = dbHandler.withHandle(handle -> handle.createQuery(GET_MAX_REPAIR_TEMPLATE_ID)
                .mapTo(Integer.class)
                .one()
        );

        if (created != 1) {
            throw new ServiceException("Failed to insert repair template!");
        }

        requiredParts.forEach(pair -> dbHandler.withHandle(handle -> handle.createUpdate(INSERT_REQUIRED_PART)
                .bind("partId", pair.getPart().getId())
                .bind("quantity", pair.getQuantity())
                .bind("templateId", templateId)
                .execute()
        ));
    }

    public int deleteRepairTemplate(Integer templateId) {
        return dbHandler.withHandle(handle -> handle.createUpdate(DELETE_REPAIR_TEMPLATE)
                .bind("id", templateId)
                .execute()
        );
    }

    public List<String> getAllPositions() {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_POSITIONS)
                .mapTo(String.class)
                .list()
        );
    }

    public int addPosition(String name) {
        return dbHandler.withHandle(handle -> handle.createUpdate(INSERT_POSITION)
                .bind("name", name)
                .execute()
        );
    }

    public int deleteEmployee(Integer id) {
        return dbHandler.withHandle(handle -> handle.createUpdate(DELETE_EMPLOYEE)
                .bind("is", id)
                .execute()
        );
    }

    public Iterator<Employee> getAllEmployees() {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_ALL_EMPLOYEES)
                .map(new EmployeeMapper())
                .iterator()
        );
    }

    public Iterator<Employee> getEmployeesByFullName(String name, String surname) {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_EMPLOYEES_BY_FULL_NAME)
                .bind("name", name)
                .bind("surname", surname)
                .map(new EmployeeMapper())
                .iterator()
        );
    }

    public Iterator<Employee> getEmployeesByPosition(String position) {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_EMPLOYEES_BY_POSITION)
                .bind("position", position)
                .map(new EmployeeMapper())
                .iterator()
        );
    }

    public List<Employee> getEmployeeByFullNameAndPosition(String name, String surname, String position) {
        return dbHandler.withHandle(handle -> handle.createQuery(GET_EMPLOYEES_BY_FULL_NAME_AND_POSITION)
                .bind("name", name)
                .bind("surname", surname)
                .bind("position", position)
                .map(new EmployeeMapper())
                .list()
        );
    }

    public int addEmployee(Employee employee) {
        var positionId = dbHandler.withHandle(handle -> handle.createQuery(GET_POSITION_ID_BY_NAME)
                .bind("name", employee.getPosition())
                .mapTo(Integer.class)
                .one()
        );
        return dbHandler.withHandle(handle -> handle.createUpdate(INSERT_EMPLOYEE)
                .bind("name", employee.getName())
                .bind("surname", employee.getSurname())
                .bind("positionId", positionId)
                .execute()
        );
    }

    public int updatePosition(Employee employee, String newPosition) {
        var positionId = dbHandler.withHandle(handle -> handle.createQuery(GET_POSITION_ID_BY_NAME)
                .bind("name", newPosition)
                .mapTo(Integer.class)
                .one()
        );

        return dbHandler.withHandle(handle -> handle.createUpdate(UPDATE_EMPLOYEE_POSITION)
                .bind("positionId", positionId)
                .bind("id", employee.getId())
                .execute()
        );
    }

    public boolean positionExists(String name) {
        return !dbHandler.withHandle(handle ->
                handle.createQuery(POSITION_EXISTS)
                        .bind("name", name)
                        .mapTo(Integer.class)
                        .list()
                        .isEmpty()
        );
    }

    public boolean templateExists(String name) {
        return !dbHandler.withHandle(handle ->
                handle.createQuery(TEMPLATE_EXISTS)
                        .bind("name", name)
                        .mapTo(Integer.class)
                        .list()
                        .isEmpty()
        );
    }
}
