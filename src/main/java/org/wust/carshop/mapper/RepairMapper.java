package org.wust.carshop.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.wust.carshop.model.Car;
import org.wust.carshop.model.Employee;
import org.wust.carshop.model.Repair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RepairMapper implements RowMapper<Repair> {
    @Override
    public Repair map(ResultSet rs, StatementContext ctx) throws SQLException {
        var client = new ClientMapper().map(rs, ctx);

        var car = Car.builder()
                .VIN(rs.getString("VIN"))
                .owner(client)
                .color(rs.getString("color"))
                .model(rs.getString("model"))
                .brand(rs.getString("brand"))
                .productionYear(rs.getInt("production_year"))
                .build();

        var employee = Employee.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("p_name"))
                .surname(rs.getString("p_surname"))
                .position(rs.getString("p_position"))
                .build();

        return Repair.builder()
                .id(rs.getInt("r_id"))
                .repairedCar(car)
                .mechanic(employee)
                .startDate(LocalDate.parse(rs.getString("start_date")))
                .build();
    }
}
