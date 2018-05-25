package com.kalix.framework.core.api.web;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.web.model.LoginBean;
import com.kalix.framework.core.api.web.model.MenuBean;
import com.kalix.framework.core.api.web.model.ModuleBean;
import com.kalix.framework.core.api.web.model.WebApplicationBean;

import java.util.List;
import java.util.Map;

/**
 * Created by sunlf on 2015/7/13.
 * osgi rest service
 */
public interface ISystemService {
    /**
     * 返回所有应用
     * @return
     */
    List<WebApplicationBean> getApplicationList();

    /**
     * 返回指定应用下模块列表
     * @param applicationId
     * @return
     */
    List<ModuleBean> getModuleByApplication(String applicationId);

    /**
     * 返回指定应用下模块列表(不考虑权限、菜单，仅返回模块列表)
     * @param appId：ETC菜单配置文件APPLICATION_APP_ID值
     * @return
     */
    //List<ModuleBean> getModulesByAppId(String appId);
    JsonData getModulesByAppId(String appId);

    /**
     * 新增指定应用下模块
     * @param appId
     * @param bean
     * @return
     */
    JsonStatus addModuleByAppId(String appId, ModuleBean bean);

    /**
     * 修改指定应用、指定配置文件key下模块配置
     * @param appId
     * @return
     */
    JsonStatus setModuleByAppId(String appId, String cfgKey, ModuleBean bean);

    /**
     * 删除指定应用、指定配置文件key下模块配置
     * @param appId
     * @param cfgKey
     * @return
     */
    JsonStatus deleteModuleByAppId(String appId, String cfgKey);

    /**
     * 返回指定模块下菜单
     * @param moduleId
     * @return
     */
    MenuBean getMenuByModule(String moduleId);

    /**
     * 返回指定模块下菜单列表(不考虑权限、子菜单，仅返回一级菜单列表)
     * @param moduleId：ETC菜单配置文件Module的_ID值
     * @return
     */
    //List<MenuBean> getMenusByModuleId(String moduleId);
    JsonData getMenusByModuleId(String moduleId);

    /**
     * 新增指定模块下一级菜单
     * @param moduleId
     * @param bean
     * @return
     */
    JsonStatus addMenuByModuleId(String moduleId, MenuBean bean);

    /**
     * 修改指定模块、指定配置文件key下一级菜单配置
     * @param moduleId
     * @param cfgKey
     * @param bean
     * @return
     */
    JsonStatus setMenuByModuleId(String moduleId, String cfgKey, MenuBean bean);

    /**
     * 删除指定模块、指定配置文件key下一级菜单配置
     * @param appId
     * @param cfgKey
     * @return
     */
    JsonStatus deleteMenuByModuleId(String appId, String cfgKey);

    /**
     * 判断按钮权限
     * 生成JSON如下:
     *    [{
     *        permission:"admin:sysModule:permissionControl:userMenu:add",
     *        status:true
     *    }]
     * @param permission
     * @return
     */
    Map getButtonsByPermission(String permission);

    /**
     * 获得登录组件信息
     * @return
     */
    LoginBean getLogin();

    /**
     * 获取用户偏好
     */
    Map getUserPreferences(String loginName);
    /**
     * 设置用户偏好
     */
    JsonStatus setUserPreferences(String loginName,String key, String value);

    //获得当前系统的登录状态 并根据具体配置返回加载页
    JsonStatus doSysServiceTest();
    //检查系统是否需要验证码
    JsonStatus doVCodeTest(String appName);

    // 通过menu名字，返回app的名称，用于数据权限
    String getAppName(String menuName);
}
