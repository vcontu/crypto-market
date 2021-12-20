package com.endava.internship.cryptomarket.confservice.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.endava.internship.cryptomarket.confservice.data.mapper.SqlArgumentsMapper;
import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import com.endava.internship.cryptomarket.confservice.data.model.User.UserBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Component
public class UserJdbcRepository extends JdbcRepository implements UserRepository{

    public UserJdbcRepository(DataSource dataSource, SqlArgumentsMapper mapper) {
        super(dataSource, mapper);
    }

    @Override
    public List<User> getAll() {
        return executeSelectQuery(UserMapper.FIND_ALL_USERS_QUERY, UserMapper::mapUsers);
    }

    @Override
    public Optional<User> get(String username) {
        return executeSelectQuery(UserMapper.FIND_USER_QUERY, UserMapper::mapUser, Map.of(1, username));
    }

    @Override
    public boolean save(User newUser) {
        Optional<User> user = executeSelectQuery(UserMapper.FIND_USER_QUERY, UserMapper::mapUser, Map.of(1, newUser.getUsername()));
        if(user.isPresent()){
            executeQueryWithArguments(UserMapper.UPDATE_QUERY, newUser, UserMapper::updateStatementConsumer, Map.of(7, newUser.getUsername()));
        }
        else {
            executeQueryWithArguments(UserMapper.INSERT_QUERY, newUser, UserMapper::insertStatementConsumer, emptyMap());
        }
        return true;
    }

    @Override
    public boolean delete(String username) {
        Optional<User> user = executeSelectQuery(UserMapper.FIND_USER_QUERY, UserMapper::mapUser, Map.of(1, username));
        if(user.isPresent()) {
            executeQueryWithArguments(UserMapper.DELETE_QUERY, Map.of(1, username));
            return true;
        }
        return false;
    }

    @Override
    public boolean exists(User user) {
        Optional<User> optionalUser = executeSelectQuery(UserMapper.FIND_USER_QUERY, UserMapper::mapUser, Map.of(1, user.getUsername()));
        return optionalUser.isPresent();
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    private static class UserMapper {

        private static final String FIND_ALL_USERS_QUERY =
                "SELECT username, email, role, status, created_on, updated_on, updated_by FROM t_user";

        private static final String FIND_USER_QUERY =
                "SELECT username, email, role, status, created_on, updated_on, updated_by FROM t_user WHERE username = ?";

        private static final String INSERT_QUERY =
                "INSERT INTO t_user(username, email, role, status, created_on) VALUES(?, ?, ?, ?, ?)";

        private static final String DELETE_QUERY =
                "DELETE FROM t_user WHERE username = ?";

        private static final String UPDATE_QUERY = "UPDATE t_user "
                + "SET email = ?, "
                + "role = ?, "
                + "status = ?, "
                + "created_on = ?, "
                + "updated_on = ?, "
                + "updated_by = ? "
                + "WHERE username = ? ";

        public static List<User> mapUsers(ResultSet rs) throws SQLException {
            List<User> userList = new ArrayList<>();

            while (rs.next()) {
                UserBuilder userBuilder = User.builder();

                userBuilder.username(rs.getString("username"));
                userBuilder.email(rs.getString("email"));
                userBuilder.role(Roles.valueOf(rs.getString("role")));
                userBuilder.status(Status.valueOf(rs.getString("status")));
                userBuilder.createdOn(rs.getTimestamp("created_on").toLocalDateTime());

                final Timestamp updatedOn = rs.getTimestamp("updated_on");
                if(nonNull(updatedOn)) {
                    userBuilder.updatedOn(updatedOn.toLocalDateTime());
                }

                final String updated_by = rs.getString("updated_by");
                if(nonNull(updated_by)) {
                    userBuilder.updatedBy(updated_by);
                }

                userList.add(userBuilder.build());
            }
            return userList;
        }

        public static Optional<User> mapUser(ResultSet rs) throws SQLException {
            final List<User> users = mapUsers(rs);
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        }

        public static void updateStatementConsumer(PreparedStatement preparedStatement, User user) throws SQLException {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getRole().toString());
            preparedStatement.setString(3, user.getStatus().toString());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(user.getCreatedOn()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(user.getUpdatedOn()));
            preparedStatement.setString(6, user.getUpdatedBy());
            preparedStatement.setString(7, user.getUsername());
        }

        public static void insertStatementConsumer(PreparedStatement preparedStatement, User user) throws SQLException {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setString(4, user.getStatus().toString());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(user.getCreatedOn()));
        }
    }
}
