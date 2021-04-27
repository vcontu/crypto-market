package com.endava.internship.cryptomarket.confservice.business.validators.orders;

import javax.validation.GroupSequence;

@GroupSequence({RequesterAccessOrder2100.class,
        UserRolesOrder3200_3300.class,
        ValidCreateOrder5300.class,
        ValidUserOrder5400.class,
        UserStatusOrder2200_2300.class,
        NonExistentOrder5400.class})
public interface CreateRequestOrder {
}
