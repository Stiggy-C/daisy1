package io.openenterprise.daisy.data.service;

import io.openenterprise.daisy.data.domain.Auditable;
import io.openenterprise.daisy.data.repository.AuditableRepository;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;

public class AbstractAuditableServiceImpl<A extends Auditable<I, U, T>, I extends Serializable, U, T extends Temporal>
        extends AbstractPersistableServiceImpl<A, I, U, T> implements AuditableService<A, I, U, T> {

    @Inject
    protected AuditableRepository<A, I, U, T> repository;

    @Override
    @Nonnull
    public A save(@Nonnull A auditable) {
        return repository.save(auditable);
    }

    @Nonnull
    @Override
    public List<A> findByUpdatedBy(@Nonnull U updatedBy) {
        return repository.findAllByUpdatedBy(updatedBy);
    }
}
