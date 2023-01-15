package org.wust.carshop;

import org.jdbi.v3.core.Jdbi;
import org.postgresql.core.Utils;
import org.postgresql.ds.PGSimpleDataSource;
import org.wust.carshop.service.AdminService;
import org.wust.carshop.service.MechanicService;
import org.wust.carshop.service.UtilsService;
import org.wust.carshop.util.PartPair;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setDatabaseName("CarShopDB");
        ds.setUser("postgres");
        ds.setPassword("docker");

        var jdbi = Jdbi.create(ds);
        var us = new UtilsService(jdbi);

        var ms = new MechanicService(jdbi,us);
        var as = new AdminService(jdbi, us);

        var parts = as.getAllParts();
        var requiredParts = List.of(new PartPair(parts.next(), 69), new PartPair(parts.next(), 69));

        var templates = ms.getActiveRepairs();
        templates.forEach(System.out::println);
//        as.addRepairTemplate("Test", requiredParts);

        //TODO Check employee methods in AdminService
        //TODO Check adding methods in all services
        //TODO Write Javadocs
        //TODO ADD logging service

    }
}