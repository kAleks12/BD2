package org.wust.carshop.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.wust.carshop.model.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Employee.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("imie"))
                .surname(rs.getString("nazwisko"))
                .position(rs.getString("nazwa"))
                .build();
    }
}