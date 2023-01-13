package org.wust.carshop.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.wust.carshop.model.RepairTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepairTemplateMapper implements RowMapper<RepairTemplate> {
    @Override
    public RepairTemplate map(ResultSet rs, StatementContext ctx) throws SQLException {
        return RepairTemplate.builder()
                .id(rs.getInt("id"))
                .cost(rs.getInt("koszt"))
                .name(rs.getString("nazwa"))
                .build();
    }
}