package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IMenu;
import com.kalix.framework.core.util.ConfigUtil;

/**
 * Created by chenyanxu on 2016/6/24.
 */
public class BaseMenuImpl implements IMenu {
    private String id;
    private String text;
    private String iconCls;
    private int index;
    private int permission;
    private Boolean supportMobild;
    private String applicationId;
    private String routeId;
    private String moduleId;
    private String buttons = "添加,add;刪除,delete;修改,edit;查看,view";

    public BaseMenuImpl() {
        String[] splits = this.getClass().getSimpleName().split("Menu");

        if (splits.length > 0) {
            String idUpper = splits[0].toUpperCase();

            if(!idUpper.equals("BASE")) {
                id = ConfigUtil.getConfigProp(idUpper + "_ID", "ConfigMenu").toString();
                applicationId = ConfigUtil.getConfigProp(idUpper + "_APPLICATION_ID", "ConfigMenu").toString();
                moduleId = ConfigUtil.getConfigProp(idUpper + "_MODULE_ID", "ConfigMenu").toString();
                text = ConfigUtil.getConfigProp(idUpper + "_TEXT", "ConfigMenu").toString();
                routeId = ConfigUtil.getConfigProp(idUpper + "_ROUTE_ID", "ConfigMenu").toString();
                iconCls = ConfigUtil.getConfigProp(idUpper + "_ICONCLS", "ConfigMenu").toString();
                index = Integer.parseInt(ConfigUtil.getConfigProp(idUpper + "_INDEX", "ConfigMenu").toString());
                permission = Integer.parseInt(ConfigUtil.getConfigProp(idUpper + "_PERMISSION", "ConfigMenu").toString());
                if (ConfigUtil.getConfigProp(idUpper + "_BUTTONS", "ConfigMenu") != null) {
                    buttons = ConfigUtil.getConfigProp(idUpper + "_BUTTONS", "ConfigMenu").toString();
                }
            }
        }

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
        return routeId;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getPermission() {
        if (1 == permission) {
            return applicationId + ":" + moduleId + ":" + id;
        }

        return "";
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String getParentMenuId() {
        return null;
    }

    /**
     * 获得菜单下按钮的定义，以,分隔，例如：add,delete,edit,view
     *
     * @return
     */
    @Override
    public String getButtons() {
        return buttons;
    }

    @Override
    public Boolean getSupportMobile() {
        return true;
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
    }
}
