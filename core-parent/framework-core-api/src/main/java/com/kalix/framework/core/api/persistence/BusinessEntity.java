package com.kalix.framework.core.api.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Created by chenyanxu on 2016/10/13.
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BusinessEntity extends PersistentEntity {
    protected Long userId;
    @Transient
    protected String userName;
    @Transient
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
