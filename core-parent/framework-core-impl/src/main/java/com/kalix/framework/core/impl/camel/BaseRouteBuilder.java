package com.kalix.framework.core.impl.camel;

import com.kalix.framework.core.api.camel.CustomRest;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import io.swagger.annotations.ApiModel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenyanxu
 *         自动注册Rest地址类
 */
public class BaseRouteBuilder extends RouteBuilder {
    private RestDefinition restDef_1;//基础查询、新增 定义
    private VerbDefinition restDef_1_get;
    private VerbDefinition restDef_1_post;
    private RestDefinition restDef_2;//单个实体查询、编辑、删除 定义
    private VerbDefinition restDef_2_get;
    private VerbDefinition restDef_2_put;
    private VerbDefinition restDef_2_delete;
    private String appName;//应用名
    private String beanName;//实体全路径名
    private List<CustomRest> rests;//自定义rest列表

    @Override
    public void configure() {
        try {
            Class cls = Class.forName(beanName);//获得实体类
            String entityName = "业务实体";//实体body参数默认值
            String beanAliasName = "";//实体类中文别名
            RestOperationParamDefinition idParam = null;//id参数定义
            RestOperationParamDefinition beanBodyParam = null;//实体body参数定义

            //获得实体类标注信息
            Annotation anno = cls.getAnnotation(ApiModel.class);

            if (anno != null) {
                beanAliasName = ((ApiModel) anno).value().split("<br>")[0];
                entityName = beanAliasName + "实体";
            }
            //配置rest使用的组件、数据类型、格式
            restConfiguration().component("servlet").bindingMode(RestBindingMode.json).apiContextPath("/api-doc")
                    .dataFormatProperty("prettyPrint", "true");

            String url = "";//基地址
            String beanShotName = "";//实体英文全小写名
            //根据应用名及实体名生成基地址
            if (appName != null && !appName.isEmpty()) {
                url = "/" + appName;
            }

            if (beanName != null && !beanName.isEmpty()) {
                beanShotName = beanName.substring(beanName.lastIndexOf(".") + 1, beanName.length() - 4).toLowerCase();
                url += "/" + beanShotName + "s";
            }
            //初始化id rest参数
            idParam = new RestOperationParamDefinition();
            idParam.setName("id");
            idParam.setType(RestParamType.path);
            idParam.setDataType("integer");
            idParam.setDescription(beanAliasName + "id");
            //初始化实体body rest参数
            beanBodyParam = new RestOperationParamDefinition();
            beanBodyParam.setName("body");
            beanBodyParam.setType(RestParamType.body);
            beanBodyParam.setDescription(entityName);

            //生成基础分页查询请求参数列表
            List<RestOperationParamDefinition> params = new ArrayList<>();
            //页码参数
            params.add(new RestOperationParamDefinition());
            params.get(0).setName("page");
            params.get(0).setDataType("integer");
            params.get(0).setType(RestParamType.query);
            params.get(0).setDescription("页码");
            params.get(0).setDefaultValue("0");
            params.get(0).setRequired(true);
            //每页记录数参数
            params.add(new RestOperationParamDefinition());
            params.get(1).setName("limit");
            params.get(1).setDataType("integer");
            params.get(1).setType(RestParamType.query);
            params.get(1).setDescription("每页记录数");
            params.get(1).setDefaultValue("0");
            params.get(1).setRequired(true);
            params.get(1).allowableValues("0", "10", "20", "30", "40", "50");
            //查询条件：要求为json字符串格式 模糊查询用%%
            params.add(new RestOperationParamDefinition());
            params.get(2).setName("jsonStr");
            params.get(2).setDataType("string");
            params.get(2).setType(RestParamType.query);
            params.get(2).setDescription("查询条件:{\"%name%\":\"\"}");
            params.get(2).setDefaultValue("");
            params.get(2).setRequired(false);
            //排序条件：目前只支持单字段排序 数据格式为json对象数组
            params.add(new RestOperationParamDefinition());
            params.get(3).setName("sort");
            params.get(3).setDataType("string");
            params.get(3).setType(RestParamType.query);
            params.get(3).setDescription("排序:[{\"property\":\"creationDate\",\"direction\":\"ASC\"}]");
            params.get(3).setDefaultValue("");
            params.get(3).setRequired(false);

            restDef_1 = rest(url)
                    .consumes("application/json;charset=utf-8").produces("application/json;charset=utf-8");
            //分页查询
            restDef_1.get().
                    description("分页获得实体").
                    params(params).
                    outType(JsonData.class).
                    to("bean:" + beanShotName + "Service?method=getAllEntityByQuery(${header.page},${header.limit},${header.jsonStr},${header.sort})");
            restDef_1_get=restDef_1.getVerbs().get(0);
            //新增实体
            params = new ArrayList<>();
            params.add(beanBodyParam);
            restDef_1.post().
                    description("新增实体").
                    type(cls).
                    params(params).
                    outType(JsonStatus.class).
                    to("bean:" + beanShotName + "Service?method=saveEntity");
            restDef_1_post=restDef_1.getVerbs().get(1);
            //单个实体操作
            restDef_2 = rest(url + "/{id}").consumes("application/json;charset=utf-8").produces("application/json;charset=utf-8");
            //获取实体
            params = new ArrayList<>();
            params.add(idParam);
            restDef_2.get().
                    description("获得实体").
                    outType(cls).
                    params(params).
                    to("bean:" + beanShotName + "Service?method=getEntity(${header.id})");
            restDef_2_get=restDef_2.getVerbs().get(0);
            //删除实体
            restDef_2.delete().
                    description("删除实体").
                    outType(JsonStatus.class).
                    params(params).
                    to("bean:" + beanShotName + "Service?method=deleteEntity(${header.id})");
            restDef_2_delete=restDef_2.getVerbs().get(1);
            //编辑实体
            params = new ArrayList<>();
            params.add(idParam);
            params.add(beanBodyParam);
            restDef_2.put().
                    description("更新实体").
                    type(cls).
                    outType(JsonStatus.class).
                    params(params).
                    to("bean:" + beanShotName + "Service?method=updateEntity(${header.id},${body})");
            restDef_2_put=restDef_2.getVerbs().get(2);
            //构建自定义Rest地址
            if (rests != null && rests.size() > 0) {
                for (CustomRest customRest : rests) {
                    RestDefinition temp = null;
                    RestOperationParamDefinition nameParam = null;
                    params = new ArrayList<>();

                    //检查是否与基础地址重复
                    if (url.equals(customRest.getPath())) {
                        if(customRest.getGetToUri()!=null){
                            restDef_1.getVerbs().remove(restDef_1_get);
                        }
                        else if(customRest.getPostToUri()!=null){
                            restDef_1.getVerbs().remove(restDef_1_post);
                        }


                        temp = restDef_1;
                    }
                    else if(customRest.getPath().equals(url+"/{id}")){
                        if(customRest.getGetToUri()!=null){
                            restDef_2.getVerbs().remove(restDef_2_get);
                        }
                        else if(customRest.getDeleteToUri()!=null){
                            restDef_2.getVerbs().remove(restDef_2_delete);
                        }
                        else if(customRest.getPutToUri()!=null){
                            restDef_2.getVerbs().remove(restDef_2_put);
                        }

                        temp=restDef_2;
                    }
                    else {
                        temp = rest(customRest.getPath())
                                .consumes("application/json;charset=utf-8")
                                .produces("application/json;charset=utf-8")
                                .description(customRest.getDescription());
                    }

                    //自动追加必要的地址参数
                    if (customRest.getPath().contains("{id}")) {
                        params.add(idParam);
                    }

                    if (customRest.getPath().contains("{name}")) {
                        nameParam = new RestOperationParamDefinition();

                        nameParam.setName("name");
                        nameParam.setType(RestParamType.path);
                        nameParam.setDescription(beanAliasName + "名");
                        params.add(nameParam);
                    }

                    //获得body实体类
                    Class inType = null;

                    if (customRest.getInType() != null) {
                        inType = Class.forName(customRest.getInType());
                    }

                    //get请求
                    if (customRest.getGetToUri() != null) {
                        temp.get().to(customRest.getGetToUri());

                        if (customRest.getGetOutType() != null) {
                            temp.outType(Class.forName(customRest.getGetOutType()));
                        } else {
                            temp.outType(JsonData.class);
                        }


                        temp.params(initParams(params, customRest.getGetParams()));
                        temp.description(customRest.getGetDescription());
                    }

                    if (customRest.getPostToUri() != null) {
                        temp.post().type(inType).to(customRest.getPostToUri());

                        if (customRest.getPostOutType() != null) {
                            temp.outType(Class.forName(customRest.getPostOutType()));
                        } else {
                            temp.outType(JsonStatus.class);
                        }

                        temp.params(initParams(params, customRest.getPostParams()));
                        temp.description(customRest.getPostDescription());
                    }

                    if (customRest.getPutToUri() != null) {
                        temp.put().type(inType).to(customRest.getPutToUri());

                        if (customRest.getPutOutType() != null) {
                            temp.outType(Class.forName(customRest.getPutOutType()));
                        } else {
                            temp.outType(JsonStatus.class);
                        }

                        temp.params(initParams(params, customRest.getPutParams()));
                        temp.description(customRest.getPutDescription());
                    }

                    if (customRest.getDeleteToUri() != null) {
                        temp.delete().to(customRest.getDeleteToUri());

                        if (customRest.getDeleteOutType() != null) {
                            temp.outType(Class.forName(customRest.getDeleteOutType()));
                        } else {
                            temp.outType(JsonStatus.class);
                        }

                        temp.params(initParams(params, customRest.getDeleteParams()));
                        temp.description(customRest.getDeleteDescription());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public List<CustomRest> getRests() {
        return rests;
    }

    public void setRests(List<CustomRest> rests) {
        this.rests = rests;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    //自动生成自定义rest地址参数列表
    private List<RestOperationParamDefinition> initParams(List<RestOperationParamDefinition> params, String pStr) {
        if (pStr != null && !pStr.isEmpty()) {
            String[] paramArray = pStr.split(",");
            List<RestOperationParamDefinition> rtn = null;

            rtn = new ArrayList<>();

            for (RestOperationParamDefinition ropd : params) {
                rtn.add(ropd);
            }

            if (paramArray != null && paramArray.length > 0) {
                for (String param : paramArray) {
                    String[] info = param.split(":");

                    if (info != null && info.length == 5) {
                        RestOperationParamDefinition tempROPD = new RestOperationParamDefinition();

                        tempROPD.setName(info[0]);

                        switch (info[1]) {
                            case "path":
                                tempROPD.setType(RestParamType.path);
                                break;
                            case "query":
                                tempROPD.setType(RestParamType.query);
                                tempROPD.setRequired(Boolean.valueOf(info[3]));
                                break;
                            case "body":
                                tempROPD.setType(RestParamType.body);
                                break;
                        }

                        tempROPD.setDataType(info[2]);
                        tempROPD.setDescription(info[4]);

                        rtn.add(tempROPD);
                    }
                }
            }

            return rtn;
        }

        return params;
    }
}
