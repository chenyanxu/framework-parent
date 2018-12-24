package com.kalix.framework.core.api.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kalix.framework.core.api.exception.StaleEntityException;
import io.swagger.annotations.ApiModelProperty;
import org.dozer.DozerBeanMapper;

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
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PersistentEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "kalix_id_seq", sequenceName = "kalix_id_seq", initialValue = 1000000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kalix_id_seq")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name="id", columnDefinition="integer NOT NULL default 'nextval('kalix_id_seq')")
    @ApiModelProperty(value = "实体ID（新增0）", hidden = true)
    private long id;
    @Version
    @ApiModelProperty(value = "版本号", hidden = true)
    private long version_;
    @ApiModelProperty(value = "创建日期", hidden = true)
    private Date creationDate;// 创建日期
    @ApiModelProperty(value = "创建者", hidden = true)
    private String createBy;    // 创建者
    @ApiModelProperty(value = "更新者", hidden = true)
    private String updateBy;    // 更新者
    @ApiModelProperty(value = "创建者Id", hidden = true)
    private Long createById; //创建者Id
    @ApiModelProperty(value = "更新者Id", hidden = true)
    private Long updateById; //更新者Id
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateDate = new Date();

    public PersistentEntity() {
    }

    public PersistentEntity(PersistentEntity obj) {
        new DozerBeanMapper().map(obj, this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion_() {
        return version_;
    }

    public void setVersion(long vers) {
        if (vers < version_) {
            throw new StaleEntityException(this);
        }
        this.version_ = vers;
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

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long createById) {
        this.createById = createById;
    }

    public Long getUpdateById() {
        return updateById;
    }

    public void setUpdateById(Long updateById) {
        this.updateById = updateById;
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

    public static String getTableName(Class cls) {
        Table tb = (Table) cls.getAnnotation(Table.class);

        if (tb != null) {
            return tb.name();
        }

        return null;
    }
}