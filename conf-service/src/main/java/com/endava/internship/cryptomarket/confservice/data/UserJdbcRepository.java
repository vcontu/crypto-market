package com.endava.internship.cryptomarket.confservice.data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.endava.internship.cryptomarket.confservice.data.mapper.UserRowMapper;
import com.endava.internship.cryptomarket.confservice.data.model.User;

@Component
public class UserJdbcRepository extends JdbcRepository implements UserRepository{

    public static final String USERNAME_COLUMN = "username";

    private static final String FIND_ALL_USERS_QUERY =
            "SELECT username, email, role, status, created_on, updated_on, updated_by FROM t_user";

    private static final String FIND_USER_QUERY =
            "SELECT username, email, role, status, created_on, updated_on, updated_by FROM t_user WHERE username = :username";

    private static final String INSERT_QUERY =
            "INSERT INTO t_user(username, email, role, status, created_on) VALUES(:username, :email, :role, :status, :createdOn)";

    private static final String DELETE_QUERY =
            "DELETE FROM t_user WHERE username = :username";

    private static final String UPDATE_QUERY = "UPDATE t_user "
            + "SET email = :email, "
            + "role = :role, "
            + "status = :status, "
            + "created_on = :createdOn, "
            + "updated_on = :updatedOn, "
            + "updated_by = :updatedBy "
            + "WHERE username = :username ";

    public static final String EMAIL_COLUMN = "email";

    public static final String ROLE_COLUMN = "role";

    public static final String STATUS_COLUMN = "status";

    public static final String CREATED_ON_COLUMN = "createdOn";

    public static final String UPDATED_ON_COLUMN = "updatedOn";

    public static final String UPDATED_BY_COLUMN = "updatedBy";

    private final UserRowMapper userMapper;

    public UserJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, UserRowMapper userMapper) {
        super(namedParameterJdbcTemplate);
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAll() {
        return executeSelectQuery(FIND_ALL_USERS_QUERY, userMapper);
    }

    @Override
    public Optional<User> get(String username) {
        return executeSelectQuery(FIND_USER_QUERY, userMapper, Map.of(USERNAME_COLUMN, username));
    }

    @Override
    public boolean save(User newUser) {
        Optional<User> user = executeSelectQuery(FIND_USER_QUERY, userMapper, Map.of(USERNAME_COLUMN, newUser.getUsername()));
        if(user.isPresent()){
            executeQueryWithArguments(UPDATE_QUERY, getUpdateArguments(newUser));
        }
        else {
            executeQueryWithArguments(INSERT_QUERY, getInsertArguments(newUser));
        }
        return true;
    }

    @Override
    public boolean delete(String username) {
        Optional<User> user = executeSelectQuery(FIND_USER_QUERY, userMapper, Map.of(USERNAME_COLUMN, username));
        if(user.isPresent()) {
            executeQueryWithArguments(DELETE_QUERY, Map.of(USERNAME_COLUMN, username));
            return true;
        }
        return false;
    }

    @Override
    public boolean exists(User user) {
        Optional<User> optionalUser = executeSelectQuery(FIND_USER_QUERY, userMapper, Map.of(USERNAME_COLUMN, user.getUsername()));
        return optionalUser.isPresent();
    }

    private Map<String, Object> getInsertArguments(User user){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(USERNAME_COLUMN, user.getUsername());
        arguments.put(EMAIL_COLUMN, user.getEmail());
        arguments.put(ROLE_COLUMN, user.getRole().toString());
        arguments.put(STATUS_COLUMN, user.getStatus().toString());
        arguments.put(CREATED_ON_COLUMN, Timestamp.valueOf(user.getCreatedOn()));
        return arguments;
    }

    private Map<String, Object> getUpdateArguments(User user){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(USERNAME_COLUMN, user.getUsername());
        arguments.put(EMAIL_COLUMN, user.getEmail());
        arguments.put(ROLE_COLUMN, user.getRole().toString());
        arguments.put(STATUS_COLUMN, user.getStatus().toString());
        arguments.put(CREATED_ON_COLUMN, Timestamp.valueOf(user.getCreatedOn()));
        arguments.put(UPDATED_ON_COLUMN, Timestamp.valueOf(user.getUpdatedOn()));
        arguments.put(UPDATED_BY_COLUMN, user.getUpdatedBy());
        return arguments;
    }
}
