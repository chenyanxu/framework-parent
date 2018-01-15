package com.kalix.framework.core.api.config;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;

/**
 * Created by fwb on 2018-01-15.
 * 配置服务接口类
 */
public interface IAdminConfigService extends IService {
    /**
     * 获取配置信息
     *
     * @return
     */
    JsonData getConfigInfo(String configid);

    /**
     * 保存配置信息
     *
     * @return
     */
    JsonStatus configureConfigInfo(String content,String configid);
}
