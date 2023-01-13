package org.wust.carshop.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.wust.carshop.model.Part;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartMapper implements RowMapper<Part> {
    @Override
    public Part map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Part.builder()
                .id(rs.getInt("id"))
                .price(rs.getDouble("price"))
                .category(rs.getString("type"))
                .producer(rs.getString("producer"))
                .carModel(rs.getString("car_model"))
                .carBrand(rs.getString("car_brand"))
                .serialNumber(rs.getString("serial_number"))
                .build();
    }
}