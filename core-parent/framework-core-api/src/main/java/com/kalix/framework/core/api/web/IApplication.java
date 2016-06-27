package com.kalix.framework.core.api.web;

import java.util.List;

/**
 * 应用级别接口
 */
public interface IApplication extends IBaseWebPage {
    /**
     * 返回模块列表
     * @return
     */
    List<IModule> getModules();
}
