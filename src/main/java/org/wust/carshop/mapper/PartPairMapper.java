package org.wust.carshop.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.wust.carshop.util.PartPair;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartPairMapper implements RowMapper<PartPair> {
    @Override
    public PartPair map(ResultSet rs, StatementContext ctx) throws SQLException {
        var part = new PartMapper().map(rs, ctx);
        var quantity = rs.getInt("quantity");

        return new PartPair(part, quantity);
    }
}
