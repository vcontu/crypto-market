package com.endava.internship.cryptomarket.confservice.data.mapper.mapping;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class LocalDateTimeMapping implements Mapping {

    @Override
    public boolean evaluate(final Object value) {
        return value instanceof LocalDateTime;
    }

    @Override
    public void map(final Map.Entry<Integer, Object> entry, final PreparedStatement statement) throws SQLException {
        statement.setTimestamp(entry.getKey(), Timestamp.valueOf((LocalDateTime) entry.getValue()));
    }
}
