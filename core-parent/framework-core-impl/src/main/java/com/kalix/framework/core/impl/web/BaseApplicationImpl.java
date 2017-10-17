package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IApplication;
import com.kalix.framework.core.api.web.IMenu;
import com.kalix.framework.core.api.web.IModule;
import com.kalix.framework.core.util.ConfigUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.*;

/**
 * Created by chenyanxu on 2016/6/24.
 */
public class BaseApplicationImpl implements IApplication {
    private String id;
    private String text;
    private String iconCls;
    private Boolean supportMobile;
    private int index;
    private int permission;
    private BundleContext bundleContext;
    private Map<String,ServiceRegistration> sr=new HashMap<>();

    public BaseApplicationImpl() {
        updateConfig();
    }

    public BaseApplicationImpl(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public void updateConfig() {
        String[] splits = this.getClass().getSimpleName().split("Application");

        if (splits.length > 0) {
            String idUpper = splits[0].toUpperCase();

            id = ConfigUtil.getConfigProp(idUpper + "_ID", "ConfigApplication").toString();
            text = ConfigUtil.getConfigProp(idUpper + "_TEXT", "ConfigApplication").toString();
            iconCls = ConfigUtil.getConfigProp(idUpper + "_ICONCLS", "ConfigApplication").toString();
            index = Integer.parseInt(ConfigUtil.getConfigProp(idUpper + "_INDEX", "ConfigApplication").toString());
            permission = Integer.parseInt(ConfigUtil.getConfigProp(idUpper + "_PERMISSION", "ConfigApplication").toString());
            supportMobile = Boolean.parseBoolean(ConfigUtil.getConfigProp(idUpper + "_SUPPORTMOBILE", "ConfigApplication").toString());
        }
    }

    @Override
    public void updateConfig(Dictionary<String, String> dictionary) {
        if (dictionary != null && dictionary.size() > 0) {
            Enumeration<String> keys = dictionary.keys();
            List<String> ids = new ArrayList<>();
            Dictionary<String,String> propertys=null;

            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String keySplit[] = key.split("_");
                boolean registerService=false;

                if (keySplit.length == 3 && keySplit[2].equals("ID")) {
                    ids.add(key);

                    switch (keySplit[0]) {
                        case "APPLICATION":
                            id = dictionary.get(key);
                            text = dictionary.get("APPLICATION_APP_TEXT");
                            iconCls = dictionary.get("APPLICATION_APP_ICONCLS");
                            index = Integer.parseInt(dictionary.get("APPLICATION_APP_INDEX"));
                            permission = Integer.parseInt(dictionary.get("APPLICATION_APP_PERMISSION"));
                            supportMobile = Boolean.parseBoolean(dictionary.get("APPLICATION_APP_SUPPORTMOBILE"));
                            break;
                        case "MENU":
                            BaseMenuImpl menu = null;

                            String menuId = dictionary.get(key);
                            String menuApplicationId = dictionary.get("MENU_" + keySplit[1] + "_APPLICATION_ID");
                            String menuModuleId = dictionary.get("MENU_" + keySplit[1] + "_MODULE_ID");
                            String menuText = dictionary.get("MENU_" + keySplit[1] + "_TEXT");
                            String menuRouteId = dictionary.get("MENU_" + keySplit[1] + "_ROUTE_ID");
                            String menuIconCls = dictionary.get("MENU_" + keySplit[1] + "_ICONCLS");
                            String menuIndex = dictionary.get("MENU_" + keySplit[1] + "_INDEX");
                            String menuPermission = dictionary.get("MENU_" + keySplit[1] + "_PERMISSION");
                            String menuButtons = dictionary.get("MENU_" + keySplit[1] + "_BUTTONS");

                            if (
                                    menuId != null &&
                                            menuApplicationId != null &&
                                            menuModuleId != null &&
                                            menuText != null &&
                                            menuRouteId != null &&
                                            menuIconCls != null && menuIndex != null &&
                                            menuPermission != null) {
                                if (sr.get(key) == null) {
                                    menu = new BaseMenuImpl();
                                    propertys=new Hashtable<>();
                                    propertys.put("MENU_ID",menuId);
                                    registerService=true;
                                } else {
                                    menu= (BaseMenuImpl) bundleContext.getService(sr.get(key).getReference());
                                    bundleContext.ungetService(sr.get(key).getReference());
                                }

                                menu.setId(menuId);
                                menu.setApplicationId(menuApplicationId);
                                menu.setModuleId(menuModuleId);
                                menu.setText(menuText);
                                menu.setRouteId(menuRouteId);
                                menu.setIconCls(menuIconCls);
                                menu.setIndex(Integer.parseInt(menuIndex));
                                menu.setPermission(Integer.parseInt(menuPermission));

                                if (menuButtons != null) {
                                    menu.setButtons(menuButtons);
                                }

                                if(registerService){
                                    sr.put(key,bundleContext.registerService(IMenu.class.getName(), menu, propertys));
                                }
                            }

                            break;
                        case "MODULE":
                            BaseModuleImpl module = null;
                            String moduleId = dictionary.get(key);
                            String moduleApplicationId = dictionary.get("MODULE_" + keySplit[1] + "_APPLICATION_ID");
                            String moduleText = dictionary.get("MODULE_" + keySplit[1] + "_TEXT");
                            String moduleIconCls = dictionary.get("MODULE_" + keySplit[1] + "_ICONCLS");
                            String moduleIndex = dictionary.get("MODULE_" + keySplit[1] + "_INDEX");
                            String modulePermission = dictionary.get("MODULE_" + keySplit[1] + "_PERMISSION");

                            if (
                                    moduleId != null &&
                                    moduleApplicationId != null &&
                                    moduleText != null &&
                                    moduleIconCls != null &&
                                    moduleIndex != null &&
                                    modulePermission != null) {
                                if (sr.get(key) == null) {
                                    module = new BaseModuleImpl();
                                    propertys=new Hashtable<>();
                                    propertys.put("MODULE_ID",moduleId);
                                    registerService=true;
                                } else {
                                    module= (BaseModuleImpl) bundleContext.getService(sr.get(key).getReference());
                                    bundleContext.ungetService(sr.get(key).getReference());
                                }

                                module.setId(moduleId);
                                module.setApplicationId(moduleApplicationId);
                                module.setText(moduleText);
                                module.setIconCls(moduleIconCls);
                                module.setIndex(Integer.parseInt(moduleIndex));
                                module.setPermission(Integer.parseInt(modulePermission));

                                if(registerService){
                                    sr.put(key,bundleContext.registerService(IModule.class.getName(), module, propertys));
                                }
                            }

                            break;
                    }
                }
            }

            List<String> unRegistKeyList=new ArrayList<>();

            sr.keySet().stream().filter(id -> !ids.contains(id)).forEach(id -> {
                unRegistKeyList.add(id);
            });

            for (String key:unRegistKeyList) {
                sr.get(key).unregister();
                sr.remove(key);
            }
        }
    }

    @Override
    public List<IModule> getModules() {
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getIconCls() {
        return iconCls;
    }

    @Override
    public String getRouteId() {
        return "";
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Boolean getSupportMobile() {
        return supportMobile;
    }

    public void setSupportMobile(Boolean supportMobile) {
        this.supportMobile = supportMobile;
    }

    @Override
    public String getPermission() {

        if (1 == permission) {
            return id;
        }
        return "";
    }
}
