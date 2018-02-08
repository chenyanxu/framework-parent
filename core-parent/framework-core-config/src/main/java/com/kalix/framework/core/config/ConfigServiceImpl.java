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
    public JsonData getConfigInfo(String AppName) {
        JsonData jsondata = new JsonData();
        Dictionary<String, Object> config= ConfigUtil.getAllConfig(AppName);

        ConfigBean configBean = null;
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
                    this.setConfigBean(configBean,value,keyName,config);
                }
                else
                {
                    configBean = new ConfigBean();
                    configBean.setId(key);
                    this.setConfigBean(configBean,value,keyName,config);
                    list.add(configBean);
                }
                key_str+=key+",";
            }

        }
        jsonStatus.setSuccess(true);
        for(int i=0;i<list.size()-1;i++){
            for(int j=0;j<list.size()-i-1;j++){
                ConfigBean map= (ConfigBean)list.get(j);
                ConfigBean map_j= (ConfigBean)list.get(j+1);
                if( map.getOrder()>map_j.getOrder()){
                    /*交换*/
                    // Integer temp=list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, map);
                }
            }
        }
        jsondata.setData(list);
        return jsondata;
    }

    /**
     * 设置配置信息
     *
     * @return
     */
    public  JsonStatus configureConfigInfo(String content,String AppName)
    {
        ConfigBean configBean = new ConfigBean();
        Dictionary<String, Object> config=ConfigUtil.getAllConfig(AppName);
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
        ConfigUtil.saveAllConfig(config,AppName);
        jsonStatus.setMsg("设置成功！");
        jsonStatus.setSuccess(true);
        return jsonStatus;
    }

    /**
     * 根据id获取配置信息
     *
     * @return
     */
    public JsonData getConfigInfoById(String AppName,String id) {
        JsonData jsondata = new JsonData();
        Dictionary<String, Object> config= ConfigUtil.getAllConfig(AppName);

        ConfigBean configBean = null;
        Enumeration enumeration= config.keys();
        List list = new ArrayList();
        String key_str="";
        for(Enumeration e=enumeration;e.hasMoreElements();){
            String keyName=e.nextElement().toString();

            if(!"felix.fileinstall.filename".equals(keyName)&&!"service.pid".equals(keyName))
            {
                String key=keyName.split("\\.")[0];
                String value=keyName.split("\\.")[1];
                if(key.equals(id)||"all".equals(id)){
                    if(key_str.indexOf(key)>-1)
                    {
                        this.setConfigBean(configBean,value,keyName,config);

                    }
                    else {
                            configBean = new ConfigBean();
                            configBean.setId(key);
                            this.setConfigBean(configBean,value,keyName,config);
                            Map map = new HashMap();
                            map.put(key,configBean);
                            list.add(map);
                            key_str+=key+",";

                    }

                }

            }

        }
        jsonStatus.setSuccess(true);
        doOrder(list);
        jsondata.setData(list);
        return jsondata;
    }


    public void setConfigBean(ConfigBean configBean,String value,String keyName,Dictionary<String, Object> config)
    {
        if ("desc".equals(value)) {
            configBean.setDesc(config.get(keyName).toString());
        }
        if ("name".equals(value)) {
            configBean.setName(config.get(keyName).toString());
        }
        if ("type".equals(value)) {
            configBean.setType(config.get(keyName).toString());
        }
        if ("value".equals(value)) {
            configBean.setValue(config.get(keyName).toString());
        }
        if("order".equals(value))
        {
            configBean.setOrder(Integer.parseInt(config.get(keyName).toString()));
        }
    }

    public void doOrder(List list)
    {
        for(int i=0;i<list.size()-1;i++){
            for(int j=0;j<list.size()-i-1;j++){
                Map map= (Map)list.get(j);
                ConfigBean configbean=(ConfigBean)map.values().toArray()[0];
                Map map_j= (Map)list.get(j+1);
                ConfigBean configbean_next=(ConfigBean)map_j.values().toArray()[0];
                if( configbean.getOrder()>configbean_next.getOrder()){
                    /*交换*/
                    // Integer temp=list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, map);
                }
            }
        }
    }
}
