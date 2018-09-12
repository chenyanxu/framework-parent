package com.kalix.framework.extend.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kalix.framework.core.api.persistence.PersistentEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

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
public abstract class PersistentExtendEntity extends PersistentEntity {
    @ApiModelProperty(value = "逻辑删除标识", hidden = true)
    private String delFlag = "0";
    @ApiModelProperty(value = "逻辑删除原因", hidden = true)
    private String reason;

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}