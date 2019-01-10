package com.kalix.framework.core.web.impl;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.security.IShiroService;
import com.kalix.framework.core.api.web.IApplication;
import com.kalix.framework.core.api.web.IMenu;
import com.kalix.framework.core.api.web.IModule;
import com.kalix.framework.core.api.web.ISystemService;
import com.kalix.framework.core.api.web.model.LoginBean;
import com.kalix.framework.core.api.web.model.MenuBean;
import com.kalix.framework.core.api.web.model.ModuleBean;
import com.kalix.framework.core.api.web.model.WebApplicationBean;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.framework.core.util.StringUtils;
import com.kalix.framework.core.web.Const;
import com.kalix.framework.core.web.manager.ApplicationManager;
import com.kalix.framework.core.web.manager.MenuManager;
import com.kalix.framework.core.web.manager.ModuleManager;
import org.apache.shiro.subject.Subject;


import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

import java.util.*;

/**
 * Created by sunlf on 2015/7/13.
 * 系统菜单服务实现类
 */
public class SystemServiceImpl implements ISystemService {
    private IShiroService shiroService;
    private PreferencesService preferencesService;

    public void setShiroService(IShiroService shiroService) {
        this.shiroService = shiroService;
    }

    /**
     * 返回实现IApplication接口的列表
     *
     * @return
     */
    @Override
    public List<WebApplicationBean> getApplicationList() {
        Subject subject = shiroService.getSubject();
        //Subject subject=new Subject.Builder().sessionId(sessionId).buildSubject();
        List<WebApplicationBean> applicationBeans = new ArrayList<>();
        if (subject == null)
            return applicationBeans;

        List<IApplication> applicationList = ApplicationManager.getInstall().getApplicationList();
        if (applicationList != null && applicationList.size() > 0) {
            Mapper mapper = DozerBeanMapperBuilder.buildDefault();
            for (IApplication application : applicationList) {
                //调用isPermitted不能传入空字符,故此默认值为KALIX_NOT_PERMISSION
                String permission = StringUtils.isEmpty(application.getPermission()) ? Const.KALIX_NO_PERMISSION : application.getPermission();
                //具有权限或不进行权限验证，都通过
                if (subject.hasRole(permission) || permission.equals(Const.KALIX_NO_PERMISSION)) {
                    WebApplicationBean applicationBean = mapper.map(application, WebApplicationBean.class);
                    applicationBeans.add(applicationBean);
                }
            }
        }
        return applicationBeans;
    }

    /**
     * 返回实现IModule接口的列表
     *
     * @param applicationId
     * @return
     */
    @Override
    public List<ModuleBean> getModuleByApplication(String applicationId) {
        Subject subject = shiroService.getSubject();
        //Subject subject=new Subject.Builder().sessionId(sessionId).buildSubject();
        List<IModule> moduleList = ModuleManager.getInstall().getModuleList(applicationId);
        List<ModuleBean> moduleBeanList = new ArrayList<ModuleBean>();
        if (moduleList == null)
            moduleList = new ArrayList<IModule>();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        //找出所有对应权限的功能模块
        if (moduleList != null && !moduleList.isEmpty()) {
            for (IModule module : moduleList) {
                //调用isPermitted不能传入空字符,故此默认值为KALIX_NOT_PERMISSION
                String modulePermission = StringUtils.isEmpty(module.getPermission()) ? Const.KALIX_NO_PERMISSION : module.getPermission();
                //具有权限或不进行权限验证，都通过
                if (subject.hasRole(modulePermission) || modulePermission.equals(Const.KALIX_NO_PERMISSION)) {
                    ModuleBean moduleBean = mapper.map(module, ModuleBean.class);
                    moduleBean.setText(module.getText());
                    moduleBeanList.add(moduleBean);
                }
            }
        }
        if (moduleBeanList != null && !moduleBeanList.isEmpty()) {
            for (ModuleBean moduleBean : moduleBeanList) {
                moduleBean.setChildren(new ArrayList<MenuBean>());
                List<IMenu> menuList = new ArrayList<IMenu>();
                List<IMenu> allMenu = MenuManager.getInstall().getMenuList(moduleBean.getId());
                //去掉没有权限的菜单
                if (allMenu != null && !allMenu.isEmpty()) {
                    for (IMenu menu : allMenu) {
                        //调用hasRole不能传入空字符,故此默认值为KALIX_NOT_PERMISSION
                        String menuPermission = StringUtils.isEmpty(menu.getPermission()) ? Const.KALIX_NO_PERMISSION : menu.getPermission();
                        //具有权限或不进行权限验证，都通过
                        if (subject.hasRole(menuPermission) || menuPermission.equals(Const.KALIX_NO_PERMISSION)) {
                            menuList.add(menu);
                        }
                    }
                }
                List<IMenu> rootMenus = getRootMenus(menuList);
                if (rootMenus != null && !rootMenus.isEmpty()) {
                    for (IMenu rootMenu : rootMenus) {
                        MenuBean menuBean = null;
                        if (rootMenu != null) {
                            menuBean = mapper.map(rootMenu, MenuBean.class);
                            menuBean.setText(rootMenu.getText());
                            getMenuChildren(menuBean, menuList, mapper);
                        }
                        moduleBean.getChildren().add(menuBean);
                    }
                }
            }
        }
        return moduleBeanList;
    }

    /**
     * 返回指定应用下模块列表(不考虑权限、菜单，仅返回模块列表)
     *
     * @param appId：ETC菜单配置文件APPLICATION_APP_ID值
     * @return
     */
    @Override
    public JsonData getModulesByAppId(String appId) {
        JsonData jsonData = new JsonData();
        List<IModule> moduleList = ModuleManager.getInstall().getModuleList(appId);
        List<ModuleBean> moduleBeanList = new ArrayList<ModuleBean>();
        if (moduleList != null && !moduleList.isEmpty()) {
            Mapper mapper = DozerBeanMapperBuilder.buildDefault();
            for (IModule module : moduleList) {
                ModuleBean moduleBean = mapper.map(module, ModuleBean.class);
                String id = moduleBean.getId().trim();
                String cfgKey = id.substring(0, id.length() - 6);
                moduleBean.setCfgKey(cfgKey);
                if (StringUtils.isEmpty(module.getPermission())) {
                    moduleBean.setPermission("0");
                } else {
                    moduleBean.setPermission("1");
                }
                moduleBeanList.add(moduleBean);
            }
        }
        jsonData.setData(moduleBeanList);
        return jsonData;
    }

    /**
     * 新增指定应用下模块
     *
     * @param appId
     * @param bean
     * @return
     */
    @Override
    public JsonStatus addModuleByAppId(String appId, ModuleBean bean) {
        JsonStatus jsonStatus = new JsonStatus();
        // 模块bean.getCfgKey()重复不允许添加
        List<IModule> list = ModuleManager.getInstall().getAllModules();
        for (IModule module : list) {
            String id = module.getId().trim();
            String cfgKey = id.substring(0, id.length() - 6);
            if (cfgKey.equals(bean.getCfgKey())) {
                jsonStatus.setSuccess(false);
                jsonStatus.setMsg("添加失败，该模块配置Key已经存在！");
                return jsonStatus;
            }
        }
        String configId = "config." + appId + ".app";
        Dictionary<String, Object> dictionary = ConfigUtil.getAllConfig(configId);
        String[] keys = new String[6];
        keys[0] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_ID";
        keys[1] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_APPLICATION_ID";
        keys[2] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_TEXT";
        keys[3] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_ICONCLS";
        keys[4] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_INDEX";
        keys[5] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_PERMISSION";
        String[] values = new String[6];
        values[0] = bean.getId();
        values[1] = bean.getApplicationId();
        values[2] = bean.getText();
        values[3] = bean.getIconCls();
        values[4] = String.valueOf(bean.getIndex());
        values[5] = bean.getPermission().equals("1") ? "1" : "0";
        for (int i = 0; i < 6; i++) {
            dictionary.put(keys[i], values[i]);
        }
        ConfigUtil.saveAllConfig(dictionary, configId);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("添加成功！");
        return jsonStatus;
    }

    /**
     * 修改指定应用、指定配置文件key下模块配置
     *
     * @param appId：ETC菜单配置文件APPLICATION_APP_ID值
     * @param cfgKey：ETC菜单配置文件左侧键值中间部分，用来合成修改键值，唯一且不可修改
     * @param bean
     * @return
     */
    @Override
    public JsonStatus setModuleByAppId(String appId, String cfgKey, ModuleBean bean) {
        JsonStatus jsonStatus = new JsonStatus();
        String configId = "config." + appId + ".app";
        Dictionary<String, Object> dictionary = ConfigUtil.getAllConfig(configId);
        String[] keys = new String[4];
        keys[0] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_TEXT";
        keys[1] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_ICONCLS";
        keys[2] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_INDEX";
        keys[3] = "MODULE_" + bean.getCfgKey().toUpperCase() + "_PERMISSION";
        String[] values = new String[4];
        values[0] = bean.getText();
        values[1] = bean.getIconCls();
        values[2] = String.valueOf(bean.getIndex());
        values[3] = bean.getPermission().equals("1") ? "1" : "0";
        for (int i = 0; i < 4; i++) {
            dictionary.put(keys[i], values[i]);
        }
        ConfigUtil.saveAllConfig(dictionary, configId);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("修改成功！");
        return jsonStatus;
    }

    /**
     * 删除指定应用、指定配置文件key下模块配置
     *
     * @param appId
     * @param cfgKey
     * @return
     */
    @Override
    public JsonStatus deleteModuleByAppId(String appId, String cfgKey) {
        JsonStatus jsonStatus = new JsonStatus();
        // 模块下存在菜单不允许删除
        String moduleId = cfgKey + "Module";
        JsonData jsonData = getMenusByModuleId(appId, moduleId);
        if (jsonData.getData() != null && jsonData.getData().size() > 0) {
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("删除失败，该模块下存在菜单功能不允许删除！请先删除菜单后再删除该模块！");
            return jsonStatus;
        }
        String configId = "config." + appId + ".app";
        Dictionary<String, Object> dictionary = ConfigUtil.getAllConfig(configId);
        String[] keys = new String[6];
        keys[0] = "MODULE_" + cfgKey.toUpperCase() + "_ID";
        keys[1] = "MODULE_" + cfgKey.toUpperCase() + "_APPLICATION_ID";
        keys[2] = "MODULE_" + cfgKey.toUpperCase() + "_TEXT";
        keys[3] = "MODULE_" + cfgKey.toUpperCase() + "_ICONCLS";
        keys[4] = "MODULE_" + cfgKey.toUpperCase() + "_INDEX";
        keys[5] = "MODULE_" + cfgKey.toUpperCase() + "_PERMISSION";
        for (int i = 0; i < 6; i++) {
            dictionary.remove(keys[i]);
        }
        ConfigUtil.saveAllConfig(dictionary, configId);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("删除成功！");
        return jsonStatus;
    }

    /**
     * 返回实现IMenu接口的列表
     *
     * @param moduleId
     * @return
     */
    @Override
    public MenuBean getMenuByModule(String moduleId) {
        List<IMenu> menuList = MenuManager.getInstall().getMenuList(moduleId);
        IMenu rootMenu = getRootMenu(menuList);
        /*List<String> mapFile=new ArrayList<>();
        mapFile.add("META-INF/MenuMapper.xml");*/
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        MenuBean menuBean = null;
        if (rootMenu != null) {
            menuBean = mapper.map(rootMenu, MenuBean.class);
            menuBean.setText(rootMenu.getText());
            getMenuChildren(menuBean, menuList, mapper);
        }
        return menuBean;
    }

    /**
     * 返回指定模块下菜单列表(不考虑权限、子菜单，仅返回一级菜单列表)
     *
     * @param moduleId：ETC菜单配置文件Module的_ID值
     * @return
     */
    @Override
    public JsonData getMenusByModuleId(String appId, String moduleId) {
        JsonData jsonData = new JsonData();
        List<IMenu> menuList = MenuManager.getInstall().getMenuList(moduleId);
        List<MenuBean> menuBeanList = new ArrayList<MenuBean>();
        if (menuList != null && !menuList.isEmpty()) {
            Mapper mapper = DozerBeanMapperBuilder.buildDefault();
            for (IMenu menu : menuList) {
                MenuBean menuBean = mapper.map(menu, MenuBean.class);
                String id = menuBean.getId().trim();
                String cfgKey = id.substring(0, id.length() - 4);
                menuBean.setCfgKey(cfgKey);
                if (StringUtils.isEmpty(menu.getPermission())) {
                    menuBean.setPermission("0");
                } else {
                    menuBean.setPermission("1");
                }
                menuBeanList.add(menuBean);
            }
        }
        jsonData.setData(menuBeanList);
        return jsonData;
    }

    /**
     * 新增指定模块下一级菜单
     *
     * @param moduleId
     * @param bean
     * @return
     */
    @Override
    public JsonStatus addMenuByModuleId(String appId, String moduleId, MenuBean bean) {
        JsonStatus jsonStatus = new JsonStatus();
        // 菜单bean.getCfgKey()重复不允许添加
        List<IMenu> list = MenuManager.getInstall().getAllMenus();
        for (IMenu menu : list) {
            String id = menu.getId().trim();
            String cfgKey = id.substring(0, id.length() - 4);
            if (cfgKey.equals(bean.getCfgKey())) {
                jsonStatus.setSuccess(false);
                jsonStatus.setMsg("添加失败，该菜单配置Key已经存在！");
                return jsonStatus;
            }
        }
        String configId = "config." + appId + ".app";
        Dictionary<String, Object> dictionary = ConfigUtil.getAllConfig(configId);
        String[] keys = new String[9];
        keys[0] = "MENU_" + bean.getCfgKey().toUpperCase() + "_ID";
        keys[1] = "MENU_" + bean.getCfgKey().toUpperCase() + "_APPLICATION_ID";
        keys[2] = "MENU_" + bean.getCfgKey().toUpperCase() + "_MODULE_ID";
        keys[3] = "MENU_" + bean.getCfgKey().toUpperCase() + "_TEXT";
        keys[4] = "MENU_" + bean.getCfgKey().toUpperCase() + "_ROUTE_ID";
        keys[5] = "MENU_" + bean.getCfgKey().toUpperCase() + "_ICONCLS";
        keys[6] = "MENU_" + bean.getCfgKey().toUpperCase() + "_INDEX";
        keys[7] = "MENU_" + bean.getCfgKey().toUpperCase() + "_PERMISSION";
        keys[8] = "MENU_" + bean.getCfgKey().toUpperCase() + "_BUTTONS";
        String[] values = new String[9];
        values[0] = bean.getId();
        values[1] = bean.getApplicationId();
        values[2] = bean.getModuleId();
        values[3] = bean.getText();
        values[4] = bean.getRouteId();
        values[5] = bean.getIconCls();
        values[6] = String.valueOf(bean.getIndex());
        values[7] = bean.getPermission().equals("1") ? "1" : "0";
        values[8] = bean.getButtons();
        for (int i = 0; i < 8; i++) {
            dictionary.put(keys[i], values[i]);
        }
        if (StringUtils.isNotEmpty(values[8])) {
            dictionary.put(keys[8], values[8]);
        }
        ConfigUtil.saveAllConfig(dictionary, configId);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("添加成功！");
        return jsonStatus;
    }

    /**
     * 修改指定模块、指定配置文件key下一级菜单配置
     *
     * @param moduleId
     * @param cfgKey
     * @param bean
     * @return
     */
    @Override
    public JsonStatus setMenuByModuleId(String appId, String moduleId, String cfgKey, MenuBean bean) {
        JsonStatus jsonStatus = new JsonStatus();
        String configId = "config." + appId + ".app";
        Dictionary<String, Object> dictionary = ConfigUtil.getAllConfig(configId);
        String[] keys = new String[6];
        keys[0] = "MENU_" + bean.getCfgKey().toUpperCase() + "_TEXT";
        keys[1] = "MENU_" + bean.getCfgKey().toUpperCase() + "_ROUTE_ID";
        keys[2] = "MENU_" + bean.getCfgKey().toUpperCase() + "_ICONCLS";
        keys[3] = "MENU_" + bean.getCfgKey().toUpperCase() + "_INDEX";
        keys[4] = "MENU_" + bean.getCfgKey().toUpperCase() + "_PERMISSION";
        keys[5] = "MENU_" + bean.getCfgKey().toUpperCase() + "_BUTTONS";
        String[] values = new String[6];
        values[0] = bean.getText();
        values[1] = bean.getRouteId();
        values[2] = bean.getIconCls();
        values[3] = String.valueOf(bean.getIndex());
        values[4] = bean.getPermission().equals("1") ? "1" : "0";
        values[5] = bean.getButtons();
        for (int i = 0; i < 5; i++) {
            dictionary.put(keys[i], values[i]);
        }
        if (StringUtils.isNotEmpty(values[5])) {
            dictionary.put(keys[5], values[5]);
        } else {
            dictionary.remove(keys[5]);
        }
        ConfigUtil.saveAllConfig(dictionary, configId);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("修改成功！");
        return jsonStatus;
    }

    /**
     * 删除指定模块、指定配置文件key下一级菜单配置
     *
     * @param appId
     * @param cfgKey
     * @return
     */
    @Override
    public JsonStatus deleteMenuByModuleId(String appId, String moduleId, String cfgKey) {
        JsonStatus jsonStatus = new JsonStatus();
        // 菜单不允许删除
        if (appId.equals("admin") && moduleId.equals("menuMenu")) {
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("删除失败，系统应用下菜单管理功能不允许删除！");
            return jsonStatus;
        }
        String configId = "config." + appId + ".app";
        Dictionary<String, Object> dictionary = ConfigUtil.getAllConfig(configId);
        String[] keys = new String[9];
        keys[0] = "MENU_" + cfgKey.toUpperCase() + "_ID";
        keys[1] = "MENU_" + cfgKey.toUpperCase() + "_APPLICATION_ID";
        keys[2] = "MENU_" + cfgKey.toUpperCase() + "_MODULE_ID";
        keys[3] = "MENU_" + cfgKey.toUpperCase() + "_TEXT";
        keys[4] = "MENU_" + cfgKey.toUpperCase() + "_ROUTE_ID";
        keys[5] = "MENU_" + cfgKey.toUpperCase() + "_ICONCLS";
        keys[6] = "MENU_" + cfgKey.toUpperCase() + "_INDEX";
        keys[7] = "MENU_" + cfgKey.toUpperCase() + "_PERMISSION";
        keys[8] = "MENU_" + cfgKey.toUpperCase() + "_BUTTONS";
        for (int i = 0; i < 9; i++) {
            dictionary.remove(keys[i]);
        }
        ConfigUtil.saveAllConfig(dictionary, configId);
        jsonStatus.setSuccess(true);
        jsonStatus.setMsg("删除成功！");
        return jsonStatus;
    }

    /**
     * 获得授权的button
     *
     * @param permission
     * @return
     */
    @Override
    public Map getButtonsByPermission(String permission) {
        if (permission == null || permission.isEmpty())
            return null;
        Map resp = new HashMap();
        List<Map> buttons = new ArrayList<Map>();
        Subject subject = shiroService.getSubject();
        //Subject subject=new Subject.Builder().sessionId(sessionId).buildSubject();

        if (permission.indexOf("_") != -1) {
            String[] permissions = permission.split("_");
            for (String _permission : permissions) {
                Map button = new HashMap();
                button.put("permission", _permission);
                if (subject.hasRole(_permission)) {
                    button.put("status", true);
                } else {
                    button.put("status", false);
                }
                buttons.add(button);
            }
        } else {
            Map button = new HashMap();
            button.put("permission", permission);
            if (subject.hasRole(permission)) {
                button.put("status", true);
            } else {
                button.put("status", false);
            }
            buttons.add(button);
        }
        resp.put("buttons", buttons.toArray());
        return resp;
    }

    /**
     * 获得登录页组件信息配置
     *
     * @return
     */
    @Override
    public LoginBean getLogin() {
        String color = (String) ConfigUtil.getConfigProp("color", "ConfigLogin");
        String image = (String) ConfigUtil.getConfigProp("image", "ConfigLogin");
        String component = (String) ConfigUtil.getConfigProp("component", "ConfigLogin");
        String title = (String) ConfigUtil.getConfigProp("title", "ConfigLogin");
        String favicon = (String) ConfigUtil.getConfigProp("favicon", "ConfigLogin");

        LoginBean loginBean = new LoginBean();

        loginBean.setColor(color);
        loginBean.setImage(image);
        loginBean.setComponent(component);
        loginBean.setTitle(title);
        loginBean.setFavicon(favicon);

        return loginBean;
    }

    @Override
    public Map getUserPreferences(String loginName) {
        //String loginName = this.shiroService.getSubject().getPrincipal().toString();
        Preferences userPreferences = this.preferencesService.getUserPreferences(loginName);
        Map rtn = new HashMap<String, String>();

        try {
            String[] childrenNames = userPreferences.childrenNames();

            for (int i = 0; i < childrenNames.length; ++i) {
                rtn.put(childrenNames[i], userPreferences.node(childrenNames[i]).get("value", ""));
            }
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        return rtn;
    }

    @Override
    public JsonStatus setUserPreferences(String loginName, String key, String value) {
        //String loginName = this.shiroService.getSubject().getPrincipal().toString();

        this.preferencesService.getUserPreferences(loginName).node(key).put("value", value);

        JsonStatus jsonStatus = new JsonStatus();

        jsonStatus.setSuccess(true);

        return jsonStatus;
    }

    /**
     * 递归函数加载子菜单
     *
     * @param menuBean
     * @param menuList
     */
    private void getMenuChildren(MenuBean menuBean, List<IMenu> menuList, Mapper mapper) {
        if (menuList == null || menuList.isEmpty())
            return;
        List<MenuBean> childMenuList = new ArrayList<>();

        for (IMenu menu : menuList) {
            if (menu.getParentMenuId() != null && menu.getParentMenuId().equals(menuBean.getId())) {
                MenuBean mBean = mapper.map(menu, MenuBean.class);
                mBean.setText(menu.getText());
                childMenuList.add(mBean);
                getMenuChildren(mBean, menuList, mapper);
            }
        }
        menuBean.setChildren(childMenuList);
    }

    /**
     * 获得菜单根节点
     *
     * @param menuList
     * @return
     */
    private IMenu getRootMenu(List<IMenu> menuList) {
        if (menuList == null || menuList.isEmpty())
            return null;
        for (IMenu menu : menuList) {
            if (menu.getParentMenuId() == null) {
                return menu;
            }
        }
        return null;
    }

    /**
     * 获得菜单根节点
     *
     * @param menuList
     * @return
     */
    private List<IMenu> getRootMenus(List<IMenu> menuList) {
        List<IMenu> rootMenus = new ArrayList<IMenu>();
        if (menuList == null || menuList.isEmpty())
            return rootMenus;
        for (IMenu menu : menuList) {
            if (menu.getParentMenuId() == null) {
                rootMenus.add(menu);
            }
        }
        return rootMenus;
    }

    public PreferencesService getPreferencesService() {
        return preferencesService;
    }

    public void setPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    @Override
    public JsonStatus doSysServiceTest() {
        JsonStatus jsonStatus = new JsonStatus();

        String userLoginName = this.shiroService.getCurrentUserLoginName();

        if (userLoginName != null) {
            jsonStatus.setSuccess(true);
        } else {
            jsonStatus.setSuccess(false);
        }

        jsonStatus.setMsg(userLoginName);


        if (Boolean.valueOf((String) ConfigUtil.getConfigProp("deploy", "ConfigSystem"))) {
            jsonStatus.setTag("/index.jsp");
        } else {
            jsonStatus.setTag("/index-debug.jsp");
        }

        return jsonStatus;
    }

    @Override
    public JsonStatus doVCodeTest(String appName) {
        JsonStatus jsonStatus = new JsonStatus();

        String configId = "Config" + StringUtils.changeFirstCharacterCase(appName, true) + "Web";

        jsonStatus.setSuccess(true);
        jsonStatus.setMsg((String) ConfigUtil.getConfigProp("vcode", configId));

        return jsonStatus;
    }

    /**
     * 通过menu名字，返回app的名称，用于数据权限
     *
     * @param menuName
     * @return
     */
    public String getAppName(String menuName) {
        List<IApplication> applicationList = ApplicationManager.getInstall().getApplicationList();
        for (IApplication application : applicationList) {
            for (IModule module : ModuleManager.getInstall().getModuleList(application.getId())) {
                for (IMenu menu : MenuManager.getInstall().getMenuList(module.getId())) {
                    if (menu.getId().toLowerCase().equals(menuName + "menu")) {
                        return application.getId();
                    }
                }
            }
        }
        return "";
    }
}
