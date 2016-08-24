package com.kalix.framework.core.api.web;

import com.kalix.framework.core.util.JNDIHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenyanxu
 */
public class WebPageUtil {
    /**
     *
     * @param beanSimpleName
     * you can get the name by entity.getClass().getSimpleName()
     * @return Success: java.util.Map  Failed:null
     * Map Key:
     * APPLICATION_ID
     * APPLICATIOIN_NAME
     * MODULE_ID
     * MODULE_NAME
     * MENU_ID
     * MENU_NAME
     */
    public static Map<String,String>  getMenuInfo(String beanSimpleName){
        Map<String,String> map=null;

        if(beanSimpleName!=null && !beanSimpleName.isEmpty()){
            String menuId=beanSimpleName.substring(0,1).toLowerCase()+beanSimpleName.substring(1,beanSimpleName.indexOf("Bean"))+"Menu";
            IApplication application=null;
            IModule module=null;
            IMenu menu=null;

            try {
                 menu= JNDIHelper.getJNDIServiceForName(IMenu.class.getName(),"(MENU_ID="+menuId+")");
                 module=JNDIHelper.getJNDIServiceForName(IModule.class.getName(),"(MODULE_ID="+menu.getModuleId()+")");
                 application=JNDIHelper.getJNDIServiceForName(IApplication.class.getName(),"(APPLICATION_ID="+module.getApplicationId()+")");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(menu!=null && module!=null && application!=null){
                map=new HashMap<>();
                map.put("APPLICATION_ID",application.getId());
                map.put("APPLICATIOIN_NAME",application.getText());
                map.put("MODULE_ID",module.getId());
                map.put("MODULE_NAME",module.getText());
                map.put("MENU_ID",menu.getId());
                map.put("MENU_NAME",menu.getText());
            }
        }

        return map;
    }
}
