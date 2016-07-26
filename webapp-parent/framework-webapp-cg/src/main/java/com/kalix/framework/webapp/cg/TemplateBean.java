package com.kalix.framework.webapp.cg;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TemplateBean implements Serializable {
    private String moduleDescription;
    private String parentArtifactId;
    private String parentGroupId;
    private String artifactIdPrefix;
    private String namePrefix;
    private String projectName;
    private String packageName;
    private String pomName;
    private String beanName;
    private String tableName;
    private String extjsPrefix;
    private String contextPath;

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String getParentArtifactId() {
        return parentArtifactId;
    }

    public void setParentArtifactId(String parentArtifactId) {
        this.parentArtifactId = parentArtifactId;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public String getArtifactIdPrefix() {
        return artifactIdPrefix;
    }

    public void setArtifactIdPrefix(String artifactIdPrefix) {
        this.artifactIdPrefix = artifactIdPrefix;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPomName() {
        return pomName;
    }

    public void setPomName(String pomName) {
        this.pomName = pomName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getExtjsPrefix() {
        return extjsPrefix;
    }

    public void setExtjsPrefix(String extjsPrefix) {
        this.extjsPrefix = extjsPrefix;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
