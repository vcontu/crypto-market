package com.endava.upskill.confservice.domain.service;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Priorities.P1.class, Priorities.P2.class, Priorities.P3.class, Default.class})
public interface Priorities {

    class P1 {}

    class P2 {}

    class P3 {}
}
