package com.endava.internship.cryptomarket.confservice.business.validators.orders;

import javax.validation.GroupSequence;

@GroupSequence({RequesterAccessOrder2100.class,
        UsernameNotTakenOrder4100.class,
        DifferentRequesterOrder4200.class,
        SameUsernameOrder4300.class,
        ValidAmendOrder5200.class,
        ValidUserOrder5400.class,
        UserStatusOrder2200_2300.class})
public interface AmendRequestOrder {
}
