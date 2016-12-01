package com.kalix.framework.core.api.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by chenyanxu on 2016/10/13.
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BusinessEntity extends PersistentEntity {
    @ApiModelProperty("用户ID")
    protected Long userId;
    @Transient
    @ApiModelProperty("用户名")
    protected String userName;
    @Transient
    @ApiModelProperty("用户头像地址")
    protected String userIcon;

    public BusinessEntity(){}

    public BusinessEntity(BusinessEntity obj,String userName,String userIcon){
        super(obj);
        this.userName=userName;
        this.userIcon=userIcon;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
