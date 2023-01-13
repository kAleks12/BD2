package org.wust.carshop.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.wust.carshop.model.Address;
import org.wust.carshop.model.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMapper implements RowMapper<Client> {
    @Override
    public Client map(ResultSet rs, StatementContext ctx) throws SQLException {
        var address = Address.builder()
                .id(rs.getInt("id"))
                .apartment(rs.getInt("numer_mieszkania"))
                .city(rs.getString("miasto"))
                .postalCode(rs.getString("kod_pocztowy"))
                .street(rs.getString("ulica"))
                .buildingNumber(rs.getString("numer_budynku"))
                .build();

        return Client.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("imie"))
                .surname(rs.getString("nazwisko"))
                .address(address)
                .build();
    }
}
