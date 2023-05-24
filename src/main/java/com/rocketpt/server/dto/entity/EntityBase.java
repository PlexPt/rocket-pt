package com.rocketpt.server.dto.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for an entity, as explained in the book "Domain Driven Design".
 * All entities in this project have an identity attribute with type Long and
 * name id. Inspired by the DDD Sample project.
 *
 * @author Christoph Knabe
 * @author plexpt
 * @see <a href=
 * "https://github.com/citerus/dddsample-core/blob/master/src/main/java/se/citerus/dddsample
 * /domain/shared/Entity.java">Entity
 * in the DDD Sample</a>
 * @since 2017-03-06
 */
@Setter
@Getter
public abstract class EntityBase {

    /**
     * This identity field has the wrapper class type Long so that an entity which
     * has not been saved is recognizable by a null identity.
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof EntityBase)) {
            return false;
        }
        if (!getClass().equals(object.getClass())) {
            return false;
        }
        final EntityBase that = (EntityBase) object;
        _checkIdentity(this);
        _checkIdentity(that);
        return this.id.equals(that.getId());
    }

    /**
     * Checks the passed entity, if it has an identity. It gets an identity only by
     * saving.
     *
     * @param entity the entity to be checked
     * @throws IllegalStateException the passed entity does not have the identity
     *                               attribute set.
     */
    private void _checkIdentity(final EntityBase entity) {
        if (entity.getId() == null) {
            throw new IllegalStateException("Comparison identity missing in entity: " + entity);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "<" + getId() + ">";
    }

}
