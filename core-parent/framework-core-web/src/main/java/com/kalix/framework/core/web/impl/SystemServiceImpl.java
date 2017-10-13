package com.kalix.framework.core.web.impl;

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
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Mapper mapper = new DozerBeanMapper();
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
        Mapper mapper = new DozerBeanMapper();
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
        Mapper mapper = new DozerBeanMapper();
        MenuBean menuBean = null;
        if (rootMenu != null) {
            menuBean = mapper.map(rootMenu, MenuBean.class);
            menuBean.setText(rootMenu.getText());
            getMenuChildren(menuBean, menuList, mapper);
        }
        return menuBean;
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
                    if (menu.getId().equals(menuName + "Menu")) {
                        return application.getId();
                    }
                }
            }
        }
        return "";
    }
}
