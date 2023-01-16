package org.wust.carshop.service;

import lombok.NoArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.postgresql.ds.PGSimpleDataSource;
import org.wust.carshop.exception.ServiceException;

import java.sql.SQLException;

@NoArgsConstructor
public class LoggingService {
    private PGSimpleDataSource ds = null;

    public Jdbi validateRole(String user, String password) {
        if (!user.equals("postgres") && !user.equals("mechanik") &&
                !user.equals("magazynier")) {

            throw new ServiceException("There is no user " + user);
        }

        if (ds == null) {
            ds = new PGSimpleDataSource();
            ds.setDatabaseName("CarShopDB");
        }

        ds.setUser(user);
        ds.setPassword(password);

        try {
            ds.getConnection();
        } catch (SQLException e) {
            throw new ServiceException("Failed to authenticate user " + user);
        }

        return Jdbi.create(ds);
    }

    public Integer validateMechanic(String login, Jdbi handler) {
       return handler.withHandle(handle -> handle.createQuery("SELECT id from pracownicy where login = :login")
               .bind("login", login)
               .mapTo(Integer.class)
               .one()
       );
    }
}
