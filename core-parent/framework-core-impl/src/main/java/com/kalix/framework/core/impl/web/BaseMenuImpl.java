package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IMenu;
import com.kalix.framework.core.api.web.IModule;
import com.kalix.framework.core.util.ConfigUtil;

import java.util.List;

/**
 * Created by chenyanxu on 2016/6/24.
 */
public abstract class BaseMenuImpl implements IMenu {
    private String id;
    private String text;
    private String iconCls;
    private int index;
    private int permission;
    private String applicationId;
    private String routeId;
    private String moduleId;

    public BaseMenuImpl(){
        String[] splits = this.getClass().getSimpleName().split("Menu");

        if(splits.length>0){
            String idUpper=splits[0].toUpperCase();

            id= ConfigUtil.getConfigProp(idUpper+"_ID","ConfigMenu").toString();
            applicationId= ConfigUtil.getConfigProp(idUpper+"_APPLICATION_ID","ConfigMenu").toString();
            moduleId=ConfigUtil.getConfigProp(idUpper+"_MODULE_ID","ConfigMenu").toString();
            text=ConfigUtil.getConfigProp(idUpper+"_TEXT","ConfigMenu").toString();
            routeId=ConfigUtil.getConfigProp(idUpper+"_ROUTE_ID","ConfigMenu").toString();
            iconCls=ConfigUtil.getConfigProp(idUpper+"_ICONCLS","ConfigMenu").toString();
            index = Integer.parseInt(ConfigUtil.getConfigProp(idUpper+"_INDEX","ConfigMenu").toString());
            permission=Integer.parseInt(ConfigUtil.getConfigProp(idUpper+"_PERMISSION","ConfigMenu").toString());
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

    @Override
    public String getModuleId() {
        return moduleId;
    }
}
