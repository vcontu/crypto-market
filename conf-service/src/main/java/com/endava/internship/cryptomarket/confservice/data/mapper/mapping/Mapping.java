package com.endava.internship.cryptomarket.confservice.data.mapper.mapping;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public interface Mapping {

    boolean evaluate(Object value);
    void map(Map.Entry<Integer, Object> value, PreparedStatement statement) throws SQLException;

}
