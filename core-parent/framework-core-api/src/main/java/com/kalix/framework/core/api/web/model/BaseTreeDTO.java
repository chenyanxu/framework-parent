package com.kalix.framework.core.api.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 树型数据模型基类
 * @author hqj date:2017-12-04
 * @version 1.0.0
 */
public class BaseTreeDTO extends BaseDTO {
    private String name;       //名称
    private String code;       //代码
    private String text;       //名称
    private Boolean leaf;      //是否是叶子节点
    private Long parentId;     //父节点id
    private String parentName; //父节点名称
    private List<BaseTreeDTO> children = new ArrayList<BaseTreeDTO>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<BaseTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<BaseTreeDTO> children) {
        this.children = children;
    }
}
