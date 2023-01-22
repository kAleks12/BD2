package org.wust.carshop;

import org.wust.carshop.service.*;

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