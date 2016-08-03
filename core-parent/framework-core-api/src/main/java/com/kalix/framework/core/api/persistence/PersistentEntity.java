package com.kalix.framework.core.api.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kalix.framework.core.api.exception.StaleEntityException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @类描述：系统中全部实体类的基础实体类
 * @创建人：sunlf
 * @创建时间：2014-3-27 下午1:01:59
 * @修改人：
 * @修改时间：
 * @修改备注：
 */

@MappedSuperclass
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PersistentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Version
    @Column(name = "version_")
    private long version;
    //@JsonFormat(shape= JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creationDate = new Date();// 创建日期
    private String createBy;    // 创建者
    private String updateBy;    // 更新者
    // @JsonFormat(shape= JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate = new Date();
    // 更新日期


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long vers) {
        if (vers < version) {
            throw new StaleEntityException(this);
        }
        this.version = vers;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(shape= JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @JsonFormat(shape= JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null) {
            return false;
        } else if (getClass().isAssignableFrom(other.getClass())) {
            if (getIdentifier() == null) {
                return false;
            } else {
                final PersistentEntity pe = (PersistentEntity) other;
                return getIdentifier().equals(pe.getIdentifier());
            }
        }

        return false;
    }

    public int hashCode() {
        if (getIdentifier() != null) {
            return getIdentifier().hashCode();
        }
        return super.hashCode();
    }

    private Serializable getIdentifier() {
        if (id == -1L) {
            return null;
        } else {
            return id;
        }
    }
}