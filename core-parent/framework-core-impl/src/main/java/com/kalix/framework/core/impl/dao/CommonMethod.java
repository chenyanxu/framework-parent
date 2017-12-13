package com.kalix.framework.core.impl.dao;

import com.kalix.framework.core.util.SerializeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/31.
 */
public class CommonMethod {

    private static String BASE_ORDERBY = " order by t.id ";

    public static String createIdsCondition(String values) {

        String jsonStr = "";
        if (!values.isEmpty()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id:in", values);
            jsonStr = SerializeUtil.serializeJson(params);
        }
        return jsonStr;
    }

    public static String createWhereCondition(String jsonStr) {

        String rtn = "";
        //组合查询条件
        Map conditions = null;
        if (jsonStr == null) {
            jsonStr = "";
        }
        if (!jsonStr.isEmpty()) {
            conditions = SerializeUtil.json2Map(jsonStr);
        }
        if (conditions != null) {
            for (Object obj : conditions.keySet()) {
                Object value = conditions.get(obj);
                if (value != null) {
                    if (obj.toString().contains("%")) {
                        obj = obj.toString().replace("%", "");
                        if (rtn.equals("")) {
                            rtn = " t." + obj + " like '%" + value + "%'";
                        }
                        else {
                            rtn = rtn + " and t." + obj + " like '%" + value + "%'";
                        }
                    }
                    else {
                        if (rtn.equals("")) {
                            rtn = " t." + obj + " = '" + value + "'";
                        }
                        else {
                            rtn = rtn + " and t." + obj + " = '" + value + "'";
                        }
                    }
                }
            }
        }
        return rtn;
    }

    public static String createWhereCondition(String jsonStr, String sort) {

        String rtn = "";

        //组合查询条件
        String condition_sql = createWhereCondition(jsonStr);

        //组合排序条件
        String orderby_sql = createSortCondition(sort);

        //合成返回查询排序条件语句
        if (condition_sql.equals("")) {
            rtn = orderby_sql;
        }
        else {
            rtn = " where " + condition_sql + orderby_sql;
        }
        return rtn;
    }

    private static String createPagedQueryCondition(Integer page, Integer limit, String sort) {

        String rtn = "";

        //组合排序条件
        String orderby_sql = createSortCondition(sort);

        //组合分页条件
        String pagelimit_sql = createPagedQueryCondition(page,limit);

        //合成分页排序查询条件
        rtn = orderby_sql + pagelimit_sql;
        return rtn;
    }

    private static String createPagedQueryCondition(Integer page, Integer limit, String jsonStr, String sort) {

        String rtn = "";

        //组合查询条件
        String condition_sql = createWhereCondition(jsonStr);

        //组合分页排序条件
        String pagedquerycondition_sql = createPagedQueryCondition(page,limit,sort);

        //合成返回查询条件语句
        if (condition_sql.equals("")) {
            rtn = pagedquerycondition_sql;
        }
        else {
            rtn = " where " + condition_sql + pagedquerycondition_sql;
        }
        return rtn;
    }

    private static String createSortCondition(String sort) {

        String rtn = "";
        //组合排序条件
        List<Map> sortList = null;
        if (sort == null) {
            sort = "";
        }
        if (!sort.isEmpty()) {
            sortList = SerializeUtil.unserializeJson(sort, List.class);
            if(sortList != null && sortList.size() == 1){
                String sortField= (String) sortList.get(0).get("property");
                String direction= (String) sortList.get(0).get("direction");
                rtn = " order by t." + sortField + " " + direction + " ";
            }
        }
        if (rtn.equals("")) {
            rtn = BASE_ORDERBY;
        }
        return rtn;
    }

    private static String createPagedQueryCondition(Integer page, Integer limit) {

        String rtn = "";
        //组合分页条件
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 0;
        }
        if (page != 0) {
            rtn = " limit " + limit + " offset " + (page-1)*limit;
        }
        return rtn;
    }
}
