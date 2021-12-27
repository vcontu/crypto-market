package com.endava.internship.cryptomarket.confservice.data.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static java.util.Objects.nonNull;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        User.UserBuilder userBuilder = User.builder();

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

        return userBuilder.build();
    }
}
