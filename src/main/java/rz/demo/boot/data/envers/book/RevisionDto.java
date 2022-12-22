package rz.demo.boot.data.envers.book;

import org.hibernate.envers.RevisionType;
import rz.demo.boot.data.envers.audit.AuditRevisionEntity;

public class RevisionDto<T> {
    private T entity;
    private AuditRevisionEntity revisionEntity;
    private RevisionType revisionType;

    public RevisionDto(T entity, AuditRevisionEntity revisionEntity, RevisionType revisionType) {
        this.entity = entity;
        this.revisionEntity = revisionEntity;
        this.revisionType = revisionType;
    }

    /**
     * The wrapper method of the {@link #entity} property.
     *
     * @return the value of the property
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Stores the value of the {@link #entity} property internally.
     *
     * @param entity the property to be stored
     */
    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**
     * The wrapper method of the {@link #revisionEntity} property.
     *
     * @return the value of the property
     */
    public AuditRevisionEntity getRevisionEntity() {
        return revisionEntity;
    }

    /**
     * Stores the value of the {@link #revisionEntity} property internally.
     *
     * @param revisionEntity the property to be stored
     */
    public void setRevisionEntity(AuditRevisionEntity revisionEntity) {
        this.revisionEntity = revisionEntity;
    }

    /**
     * The wrapper method of the {@link #revisionType} property.
     *
     * @return the value of the property
     */
    public RevisionType getRevisionType() {
        return revisionType;
    }

    /**
     * Stores the value of the {@link #revisionType} property internally.
     *
     * @param revisionType the property to be stored
     */
    public void setRevisionType(RevisionType revisionType) {
        this.revisionType = revisionType;
    }
}
