package com.kalix.framework.core.api.persistence;

public class JsonStatus {
    private static JsonStatus jsonStatus = new JsonStatus();
    Boolean success = false;
    Boolean failure = false;
    String msg;
    String tag;

    /**
     * 返回成功json状态
     *
     * @param msg
     * @return
     */
    public static JsonStatus successResult(String msg) {
        jsonStatus.setTag("");
        jsonStatus.setMsg(msg);
        jsonStatus.setSuccess(true);
        jsonStatus.setFailure(false);
        return jsonStatus;
    }

    /**
     * 返回失败json状态
     *
     * @param msg
     * @return
     */
    public static JsonStatus failureResult(String msg) {
        jsonStatus.setTag("");
        jsonStatus.setMsg(msg);
        jsonStatus.setSuccess(false);
        jsonStatus.setFailure(true);
        return jsonStatus;
    }

    //    @ApiModelProperty(value = "an identifier", required = true)
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    //    @ApiModelProperty(value = "an identifier", required = true)
    public Boolean getFailure() {
        return failure;
    }

    public void setFailure(Boolean failure) {
        this.failure = failure;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
