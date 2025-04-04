package io.openenterprise.daisy.data.domain;

import java.io.Serializable;
import java.time.temporal.Temporal;

public interface Auditable<I extends Serializable, U, T extends Temporal> extends Persistable<I, U, T>  {

    U getUpdatedBy();

    T getUpdatedInstant();

    long getVersion();
}
