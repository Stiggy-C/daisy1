package io.openenterprise.daisy.data.repository;

import io.openenterprise.daisy.data.domain.Persistable;
import jakarta.annotation.Nonnull;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.List;

@NoRepositoryBean
public interface PersistableRepository<A extends Persistable<I, U, T>, I extends Serializable, U, T extends Temporal>
        extends ListCrudRepository<A, I>, PagingAndSortingRepository<A, I> {

    @Nonnull
    List<A> findAllByCreatedBy(@Nonnull U createdBy);
}
