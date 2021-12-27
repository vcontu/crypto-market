package com.endava.internship.cryptomarket.confservice.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class JdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    protected <T> List<T> executeSelectQuery(String query, RowMapper<T> mapper) {
        try {
            return jdbcTemplate.queryForStream(query, new HashMap<>(), mapper).collect(toList());
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

    protected <T> Optional<T> executeSelectQuery(String query, RowMapper<T> mapper, Map<String, Object> arguments) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, arguments, mapper));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected void executeQueryWithArguments(String query, Map<String, Object> arguments) {
        jdbcTemplate.update(query, arguments);
    }

}
