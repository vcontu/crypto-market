package com.endava.internship.cryptomarket.confservice.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.endava.internship.cryptomarket.confservice.data.dbutils.ResultSetMapper;
import com.endava.internship.cryptomarket.confservice.data.dbutils.SQLStatementFunction;
import com.endava.internship.cryptomarket.confservice.data.dbutils.StatementEntityConsumer;
import com.endava.internship.cryptomarket.confservice.data.mapper.SqlArgumentsMapper;
import com.endava.internship.cryptomarket.confservice.data.model.exception.RepositoryException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class JdbcRepository {

    private final DataSource hikariDataSource;

    private final SqlArgumentsMapper mapper;

    protected <T> T executeSelectQuery(String query, ResultSetMapper<T> mapper) {
        return executeStatement(query, statement -> {
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapper.map(resultSet);
            }
        });
    }

    protected void executeQueryWithArguments(String query, Map<Integer, Object> arguments) {
        executeStatement(query, statement -> {
            mapSqlArguments(arguments, statement);
            return statement.executeUpdate();
        });
    }

    protected <T> void executeQueryWithArguments(String query,
                                                 @NonNull T entity,
                                                 @NonNull StatementEntityConsumer<T> mapper,
                                                 Map<Integer, Object> arguments) {
        executeStatement(query, statement -> {
            mapper.accept(statement, entity);
            mapSqlArguments(arguments, statement);
            return statement.executeUpdate();
        });
    }

    protected <T> T executeSelectQuery(String query, ResultSetMapper<T> mapper, Map<Integer, Object> arguments) {
        return executeStatement(query, statement -> {
            mapSqlArguments(arguments, statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapper.map(resultSet);
            }
        });
    }

    private <T> T executeStatement(String query, SQLStatementFunction<T> function) {
        try (Connection connection = hikariDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            return function.apply(statement);
        } catch (SQLException ex) {
            throw new RepositoryException("Connection to database failed: " + ex.getMessage(), ex);
        }
    }

    private void mapSqlArguments(Map<Integer, Object> arguments, PreparedStatement statement) throws SQLException {
        for (var entry : arguments.entrySet()) {
            mapper.process(entry, statement);
        }
    }
}
