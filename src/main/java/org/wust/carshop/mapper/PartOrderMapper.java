package org.wust.carshop.mapper;

import org.jdbi.v3.core.statement.StatementContext;
import org.wust.carshop.model.Employee;
import org.wust.carshop.model.PartOrder;

import org.jdbi.v3.core.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PartOrderMapper implements RowMapper<PartOrder> {
    @Override
    public PartOrder map(ResultSet rs, StatementContext ctx) throws SQLException {
        var employee = Employee.builder()
                .id(rs.getInt("p_id"))
                .name(rs.getString("imie"))
                .surname(rs.getString("nazwisko"))
                .position(rs.getString("position"))
                .build();

       var part = new PartMapper().map(rs, ctx);

       return PartOrder.builder()
               .orderedPart(part)
               .orderingEmployee(employee)
               .orderDate(LocalDate.parse(rs.getString("data")))
               .price(rs.getInt("cena"))
               .quantity(rs.getInt("ilosc"))
               .id(rs.getInt("z_id"))
               .build();
    }
}
