package io.openenterprise.daisy.data.repository;

import io.openenterprise.daisy.data.domain.Auditable;
import jakarta.annotation.Nonnull;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;

@NoRepositoryBean
public interface AuditableRepository<A extends Auditable<I, U, T>, I extends Serializable, U, T extends Temporal>
        extends PersistableRepository<A, I, U, T> {

    @Nonnull
    List<A> findAllByUpdatedBy(@Nonnull U updatedBy);
}
