package com.kalix.framework.core.api.dto;


/**
 * @类描述：审计管理
 * @创建人： sunlf
 * @创建时间：2015/8/27
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class AuditDTOBean {
    private String clsName; //类名称
    private String action;//操作
    private String actor;//操作人
    private String content;//操作内容
    private Object oldEntity = null;
    private Object newEntity = null;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public Object getNewEntity() {
        return newEntity;
    }

    public void setNewEntity(Object newEntity) {
        this.newEntity = newEntity;
    }

    public Object getOldEntity() {
        return oldEntity;
    }

    public void setOldEntity(Object oldEntity) {
        this.oldEntity = oldEntity;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}
