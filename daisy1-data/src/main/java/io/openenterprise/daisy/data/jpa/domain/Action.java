package io.openenterprise.daisy.data.jpa.domain;

import io.openenterprise.commons.util.UUIDUtils;
import io.openenterprise.daisy.data.jpa.MapConverter;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Action extends AbstractMutableEntity<UUID, String, Instant> {

    @Id
    @Column(nullable = false)
    protected UUID id;

    protected String operation;

    @Convert(converter = MapConverter.class)
    protected Map<String, Object> parameters;

    @Version
    @Column(name = "version", nullable = false)
    protected long version;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Action action = (Action) o;
        return getId() != null && Objects.equals(getId(), action.getId());
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
