package com.endava.internship.cryptomarket.confservice.data.mapper.mapping;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class StringMapping implements Mapping {

    @Override
    public boolean evaluate(final Object value) {
        return value instanceof String;
    }

    @Override
    public void map(final Map.Entry<Integer, Object> entry, final PreparedStatement statement) throws SQLException {
        statement.setString(entry.getKey(), (String) entry.getValue());
    }
}
