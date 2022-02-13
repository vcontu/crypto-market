package com.endava.upskill.confservice.domain.model.update;

import com.endava.upskill.confservice.domain.model.entity.Status;
import com.endava.upskill.confservice.domain.model.shared.EmailPattern;
import com.endava.upskill.confservice.domain.model.shared.Priorities;

@MinimumOnePropertyUpdated(groups = Priorities.B1.class)
public record UserUpdateDto(
        String username, //cannot be null, otherwise it would trigger SameUsernameInPathAndObject

        @EmailPattern(groups = Priorities.B2.class)
        String email,

        Status status) {
}
