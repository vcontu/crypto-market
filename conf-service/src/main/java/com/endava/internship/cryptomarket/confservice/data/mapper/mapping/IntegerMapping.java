package com.endava.internship.cryptomarket.confservice.data.mapper.mapping;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class IntegerMapping implements Mapping {

    @Override
    public boolean evaluate(final Object value) {
        return value instanceof Integer;
    }

    @Override
    public void map(final Map.Entry<Integer, Object> entry, final PreparedStatement statement) throws SQLException {
        statement.setInt(entry.getKey(), (Integer) entry.getValue());
    }
}
