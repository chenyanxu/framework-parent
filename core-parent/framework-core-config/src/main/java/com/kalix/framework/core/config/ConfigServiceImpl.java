package com.kalix.framework.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kalix.framework.core.api.config.IConfigService;
import com.kalix.framework.core.api.config.model.ConfigBean;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.ConfigUtil;

import java.util.*;

/**
 * Created by Administrator_ on 2018/1/15.
 */
public class ConfigServiceImpl implements IConfigService {
    private JsonStatus jsonStatus = new JsonStatus();
    /**
     * 获取配置信息
     *
     * @return
     */
    public JsonData getConfigInfo(String configid) {
        JsonData jsondata = new JsonData();
        Dictionary<String, Object> config= ConfigUtil.getAllConfig(configid);

        HashMap map = new HashMap();
        Enumeration enumeration= config.keys();
        List list = new ArrayList();
        String key_str="";
        for(Enumeration e=enumeration;e.hasMoreElements();){
            String keyName=e.nextElement().toString();

            if(!"felix.fileinstall.filename".equals(keyName)&&!"service.pid".equals(keyName))
            {
                String key=keyName.split("\\.")[0];
                String value=keyName.split("\\.")[1];
                if(key_str.indexOf(key)>-1)
                {
                    map.put(value,config.get(keyName));

                }
                else
                {
                    Map map_parent = new HashMap();
                    map = new HashMap();
                    map.put(value,config.get(keyName));
                    map_parent.put(key,map);
                    list.add(map_parent);
                }
                key_str+=key+",";
            }

        }
        jsonStatus.setSuccess(true);
        jsondata.setData(list);
        return jsondata;
    }

    /**
     * 设置配置信息
     *
     * @return
     */
    public  JsonStatus configureConfigInfo(String content,String configid)
    {
        ConfigBean configBean = new ConfigBean();
        Dictionary<String, Object> config=ConfigUtil.getAllConfig(configid);
        JsonParser jsonParser = new JsonParser();
        JsonElement el= jsonParser.parse(content);
        JsonObject jsonObject=el.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet=jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            JsonObject jObject = jsonObject.getAsJsonObject(entry.getKey());
            configBean.setId(jObject.getAsJsonPrimitive("id").getAsString());
            configBean.setValue(jObject.getAsJsonPrimitive("value").getAsString());
            config.put(configBean.getId()+".value", configBean.getValue());
        }
        ConfigUtil.saveAllConfig(config,configid);
        jsonStatus.setMsg("设置成功！");
        jsonStatus.setSuccess(true);
        return jsonStatus;
    }
}
