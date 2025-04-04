package io.openenterprise.daisy.data.domain;

import java.io.Serializable;
import java.time.temporal.Temporal;

public interface Persistable<I extends Serializable, U, T extends Temporal> extends org.springframework.data.domain.Persistable<I> {

    U getCreatedBy();

    T getCreatedInstant();
}
