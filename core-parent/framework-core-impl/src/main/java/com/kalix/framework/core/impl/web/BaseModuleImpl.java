package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IMenu;
import com.kalix.framework.core.api.web.IModule;
import com.kalix.framework.core.util.ConfigUtil;

import java.util.List;

/**
 * Created by chenyanxu on 2016/6/24.
 */
public class BaseModuleImpl implements IModule {
    private String id;
    private String text;
    private String iconCls;
    private int index;
    private int permission;
    private String applicationId;

    public BaseModuleImpl(){
        String[] splits = this.getClass().getSimpleName().split("Module");

        if(splits.length>0){
            String idUpper=splits[0].toUpperCase();

            if(!idUpper.equals("BASE")){
                id= ConfigUtil.getConfigProp(idUpper+"_ID","ConfigModule").toString();
                applicationId= ConfigUtil.getConfigProp(idUpper+"_APPLICATION_ID","ConfigModule").toString();
                text=ConfigUtil.getConfigProp(idUpper+"_TEXT","ConfigModule").toString();
                iconCls=ConfigUtil.getConfigProp(idUpper+"_ICONCLS","ConfigModule").toString();
                index = Integer.parseInt(ConfigUtil.getConfigProp(idUpper+"_INDEX","ConfigModule").toString());
                permission=Integer.parseInt(ConfigUtil.getConfigProp(idUpper+"_PERMISSION","ConfigModule").toString());
            }
        }
    }

    @Override
    public List<IMenu> getMenus() {
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
    public Boolean getSupportMobile() {
        return true;
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
    public String getPermission() {
        if(1==permission){
            return applicationId + ":" + id;
        }

        return "";
    }

    @Override
    public boolean isExpanded() {
        return false;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public String getApplicationId() {
        return applicationId;
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
}
