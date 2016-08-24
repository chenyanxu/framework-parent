package com.kalix.framework.core.impl.system;

import java.util.*;

public class DictConfigManager {
    private static DictConfigManager install;
    private Map<String,String> map=new HashMap<>();

    private DictConfigManager() {
    }

    public synchronized static DictConfigManager getInstall() {
        if (install == null) {
            install = new DictConfigManager();
        }
        return install;
    }

    public void add(String key, String value){
        this.map.put(key,value);
    }

    public Integer getCacheTime(String key){
        if(map.get(key)!=null){
            return Integer.valueOf(map.get(key).toString());
        }

        return 600;
    }

    public List<Map> getDictTypes(String key){
        List<Map> rtn=new ArrayList<>();
        Map tmpMap=null;

        if(map.get(key)!=null){
            for (String str :map.get(key).toString().split(",")) {
                tmpMap=new HashMap<>();
                tmpMap.put("name",str);
                rtn.add(tmpMap);
            }
        }

        return rtn;
    }
}
