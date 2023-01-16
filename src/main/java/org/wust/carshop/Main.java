package org.wust.carshop;

import org.jdbi.v3.core.Jdbi;
import org.postgresql.core.Utils;
import org.postgresql.ds.PGSimpleDataSource;
import org.wust.carshop.model.Address;
import org.wust.carshop.model.Employee;
import org.wust.carshop.model.Part;
import org.wust.carshop.service.*;
import org.wust.carshop.util.PartPair;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        var ls = new LoggingService();
        var handler = ls.validateRole("postgres", "docker");
        var id = ls.validateMechanic("Houltham", handler);

        var us = new UtilsService(handler);
        var ms = new MechanicService(handler, us, id);

        ms.getActiveRepairs().forEach(System.out::println);
        //Update permissions
    }
}