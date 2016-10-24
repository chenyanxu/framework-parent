package com.kalix.framework.core.api.system;

import com.kalix.framework.core.api.IService;

/**
 * Created by sunlf on 2016/10/24.
 * 附件系统服务接口类
 */
public interface IAttachmentService extends IService {
    /**
     * 获得返回的id
     *
     * @param value
     * @param key
     * @param type
     * @return
     */
    String addNewAttachment(String value, String key, String type);

    /**
     * 获得附件的url
     *
     * @return
     */
    String getAttachmentUrl();
}
