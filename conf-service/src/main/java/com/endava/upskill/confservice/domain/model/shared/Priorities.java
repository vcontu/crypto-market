package com.endava.upskill.confservice.domain.model.shared;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({
        Priorities.M1.class,
        Priorities.M2.class,
        Priorities.M3.class,
        Priorities.M4.class,
        Priorities.B1.class,
        Priorities.B2.class,
        Priorities.B3.class,
        Default.class})
public interface Priorities {

    class M1 {}

    class M2 {}

    class M3 {}

    class M4 {}

    class B1 {}

    class B2 {}

    class B3 {}
}
