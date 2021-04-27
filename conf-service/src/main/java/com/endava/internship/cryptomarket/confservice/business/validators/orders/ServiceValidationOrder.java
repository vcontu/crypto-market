package com.endava.internship.cryptomarket.confservice.business.validators.orders;

import javax.validation.GroupSequence;

@GroupSequence({RequesterAccessOrder2100.class,
        RequesterNotOperatOrder3100.class,
        UsernameNotTakenOrder4100.class})
public interface ServiceValidationOrder {
}
