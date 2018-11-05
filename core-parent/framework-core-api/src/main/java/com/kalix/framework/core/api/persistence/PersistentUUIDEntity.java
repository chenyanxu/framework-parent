package com.kalix.framework.core.api.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kalix.framework.core.api.exception.StaleEntityException;
import io.swagger.annotations.ApiModelProperty;
import org.dozer.DozerBeanMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
public abstract class PersistentUUIDEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid-hex")
    private String id;
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
    private String createById; //创建者Id
    @ApiModelProperty(value = "更新者Id", hidden = true)
    private String updateById; //更新者Id
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateDate = new Date();

    public PersistentUUIDEntity() {
    }

    public PersistentUUIDEntity(PersistentUUIDEntity obj) {
        new DozerBeanMapper().map(obj, this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getUpdateById() {
        return updateById;
    }

    public void setUpdateById(String updateById) {
        this.updateById = updateById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistentUUIDEntity that = (PersistentUUIDEntity) o;
        return version_ == that.version_ &&
                Objects.equals(id, that.id) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(createBy, that.createBy) &&
                Objects.equals(updateBy, that.updateBy) &&
                Objects.equals(createById, that.createById) &&
                Objects.equals(updateById, that.updateById) &&
                Objects.equals(updateDate, that.updateDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, version_, creationDate, createBy, updateBy, createById, updateById, updateDate);
    }

    public static String getTableName(Class cls) {
        Table tb = (Table) cls.getAnnotation(Table.class);

        if (tb != null) {
            return tb.name();
        }

        return null;
    }
}