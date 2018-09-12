package com.kalix.framework.extend.api.dto;

import com.kalix.framework.core.api.web.model.BaseDTO;

/**
 * 数据传输模型基类
 * 用于JSON数据
 *
 * @author hqj <br/>
 *         date:2018-9-12
 * @version 1.0.0
 */
public abstract class BaseExtendDTO extends BaseDTO {
    protected String delFlag;  // 逻辑删除标识
    protected String reason;   // 逻辑删除原因

    public BaseExtendDTO() {
    }

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
