package com.kalix.framework.core.impl.web;

import com.kalix.framework.core.api.web.IApplication;
import com.kalix.framework.core.api.web.IModule;
import com.kalix.framework.core.util.ConfigUtil;

import java.util.List;

/**
 * Created by chenyanxu on 2016/6/24.
 */
public abstract class BaseApplicationImpl implements IApplication {
    private String id;
    private String text;
    private String iconCls;
    private int index;
    private int permission;

    public BaseApplicationImpl(){
        String[] splits = this.getClass().getSimpleName().split("Application");

        if(splits.length>0){
            String idUpper=splits[0].toUpperCase();

            id= ConfigUtil.getConfigProp(idUpper+"_ID","ConfigApplication").toString();
            text=ConfigUtil.getConfigProp(idUpper+"_TEXT","ConfigApplication").toString();
            iconCls=ConfigUtil.getConfigProp(idUpper+"_ICONCLS","ConfigApplication").toString();
            index = Integer.parseInt(ConfigUtil.getConfigProp(idUpper+"_INDEX","ConfigApplication").toString());
            permission=Integer.parseInt(ConfigUtil.getConfigProp(idUpper+"_PERMISSION","ConfigApplication").toString());
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
    public String getPermission() {

        if(1==permission){
            return id;
        }
        return "";
    }
}
