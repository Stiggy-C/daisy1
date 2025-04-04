package io.openenterprise.daisy.data.service;

import io.openenterprise.daisy.data.domain.Persistable;
import io.openenterprise.daisy.data.repository.PersistableRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPersistableServiceImpl<A extends Persistable<I, U, T>, I extends Serializable, U, T extends Temporal>
        implements PersistableService<A, I, U, T> {

    @Inject
    protected PersistableRepository<A, I, U, T> repository;

    @Override
    public void delete(@Nonnull A auditable) {
        repository.delete(auditable);
    }

    @Override
    public void deleteById(@Nonnull I id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(@Nonnull I id) {
        return repository.existsById(id);
    }

    @Nonnull
    @Override
    public List<A> findByCreatedBy(@Nonnull U createdBy) {
        return repository.findAllByCreatedBy(createdBy);
    }

    @Nullable
    @Override
    public A findById(@Nonnull I id) {
        return repository.findById(id).orElse(null);
    }

    @Nonnull
    @Override
    public A save(@Nonnull A auditable) {
        if (Objects.nonNull(auditable.getId())) {
            throw new IllegalStateException("Updating an immutable");
        }

        return repository.save(auditable);
    }
}
