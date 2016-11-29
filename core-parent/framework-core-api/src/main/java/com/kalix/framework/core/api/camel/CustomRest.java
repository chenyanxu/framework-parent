package com.kalix.framework.core.api.camel;

/**
 * Created by chenyanxu on 2016/11/23.
 *
 * 自定义Rest模型，用来自动生成Rest服务地址
 */
public class CustomRest {
    private String  inType; //body实体类型
    private String getOutType;//get请求返回类型
    private String postOutType;
    private String putOutType;
    private String deleteOutType;
    private String description="";//rest地址描述
    private String getDescription="";//get方法描述
    private String postDescription="";
    private String putDescription="";
    private String deleteDescription="";
    private String path;//rest地址
    private String getToUri;//get方法映射地址
    private String postToUri;
    private String putToUri;
    private String deleteToUri;
    private String getParams;//get方法参数详情：格式 param_name:param_type:param_data_type:required:alias
    private String postParams;
    private String putParams;
    private String deleteParams;

    public String getInType() {
        return inType;
    }

    public void setInType(String inType) {
        this.inType = inType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGetToUri() {
        return getToUri;
    }

    public void setGetToUri(String getToUri) {
        this.getToUri = getToUri;
    }

    public String getPostToUri() {
        return postToUri;
    }

    public void setPostToUri(String postToUri) {
        this.postToUri = postToUri;
    }

    public String getPutToUri() {
        return putToUri;
    }

    public void setPutToUri(String putToUri) {
        this.putToUri = putToUri;
    }

    public String getDeleteToUri() {
        return deleteToUri;
    }

    public void setDeleteToUri(String deleteToUri) {
        this.deleteToUri = deleteToUri;
    }

    public String getGetOutType() {
        return getOutType;
    }

    public void setGetOutType(String getOutType) {
        this.getOutType = getOutType;
    }

    public String getPostOutType() {
        return postOutType;
    }

    public void setPostOutType(String postOutType) {
        this.postOutType = postOutType;
    }

    public String getPutOutType() {
        return putOutType;
    }

    public void setPutOutType(String putOutType) {
        this.putOutType = putOutType;
    }

    public String getDeleteOutType() {
        return deleteOutType;
    }

    public void setDeleteOutType(String deleteOutType) {
        this.deleteOutType = deleteOutType;
    }

    public String getGetDescription() {
        return getDescription;
    }

    public void setGetDescription(String getDescription) {
        this.getDescription = getDescription;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPutDescription() {
        return putDescription;
    }

    public void setPutDescription(String putDescription) {
        this.putDescription = putDescription;
    }

    public String getDeleteDescription() {
        return deleteDescription;
    }

    public void setDeleteDescription(String deleteDescription) {
        this.deleteDescription = deleteDescription;
    }

    public String getGetParams() {
        return getParams;
    }

    public void setGetParams(String getParams) {
        this.getParams = getParams;
    }

    public String getPostParams() {
        return postParams;
    }

    public void setPostParams(String postParams) {
        this.postParams = postParams;
    }

    public String getPutParams() {
        return putParams;
    }

    public void setPutParams(String putParams) {
        this.putParams = putParams;
    }

    public String getDeleteParams() {
        return deleteParams;
    }

    public void setDeleteParams(String deleteParams) {
        this.deleteParams = deleteParams;
    }
}
