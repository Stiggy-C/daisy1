package io.openenterprise.daisy.data.jpa.domain;

import io.openenterprise.commons.util.UUIDUtils;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class ActionsChain extends AbstractMutableEntity<UUID, String, Instant> {

    @Id
    @Access(AccessType.FIELD)
    @Column(nullable = false)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    protected long version;

    @ManyToMany
    @JoinTable(name = "actions_chains_actions",
            joinColumns = @JoinColumn(name = "actions_chain_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    @ToString.Exclude
    private Set<Action> actions = new LinkedHashSet<>();


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ActionsChain actionsChain = (ActionsChain) o;
        return getId() != null && Objects.equals(getId(), actionsChain.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @PrePersist
    protected void prePersist() {
        id = UUIDUtils.randomUUIDv7();
    }
}
