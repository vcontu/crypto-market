package com.endava.internship.cryptomarket.confservice.data.mapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.endava.internship.cryptomarket.confservice.data.mapper.mapping.IntegerMapping;
import com.endava.internship.cryptomarket.confservice.data.mapper.mapping.LocalDateTimeMapping;
import com.endava.internship.cryptomarket.confservice.data.mapper.mapping.Mapping;
import com.endava.internship.cryptomarket.confservice.data.mapper.mapping.StringMapping;

@Component
public class SqlArgumentsMapper {

    private static List<Mapping> mappings = new ArrayList<>();

    static {
        mappings.add(new IntegerMapping());
        mappings.add(new StringMapping());
        mappings.add(new LocalDateTimeMapping());
    }

    public void process(Map.Entry<Integer, Object> entry, PreparedStatement statement) throws SQLException {
        Mapping rule = mappings
                .stream()
                .filter(r -> r.evaluate(entry.getValue()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("No mapper for argument " + entry.getValue().getClass()));
        rule.map(entry, statement);
    }

}
