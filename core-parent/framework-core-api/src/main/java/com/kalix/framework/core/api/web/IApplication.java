package com.kalix.framework.core.api.web;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

/**
 * 应用级别接口
 */
public interface IApplication extends IBaseWebPage {
    /**
     * 返回模块列表
     * @return
     */
    List<IModule> getModules();

    void updateConfig();

    void updateConfig(Dictionary<String,String> dictionary);
}
