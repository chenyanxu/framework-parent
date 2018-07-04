package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.util.JNDIHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */
public class CodeUtil {

    private static Long DEFAULT_CODELENGTH = 3L; // 默认代码长度3位

    /*
     * 创建实体类的代码Code(实体类中存在code与parentid字段)
     * @params 实体Dao的类, 父节点id
     * @return 代码Code
     */
    public static String createCode(Class beanDaoCls, Long parentId) {
        return createCode(beanDaoCls, parentId, DEFAULT_CODELENGTH);
    }

    /*
     * 创建实体类的代码Code(实体类中存在code与parentid字段)
     * @params 实体Dao的类, 父节点id, 代码Code的长度
     * @return 代码Code
     */
    public static String createCode(Class beanDaoCls, Long parentId, Long length) {

        String rtn = "";
        IGenericDao dao = null;
        try {
            dao = (IGenericDao) JNDIHelper.getJNDIServiceForName(beanDaoCls.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通过父节点获取子节点目前代码最大值
        String tableName = dao.getTableName();
        String sql = "select Max(t.code) from " + tableName + " t where t.parentid=?1";
        List<String> lists = dao.findByNativeSql(sql, String.class, parentId);
        String maxCode = "";
        if (lists != null && lists.size() > 0) {
            maxCode = lists.get(0);
        }
        if (maxCode == null) {
            maxCode = "";
        }
        maxCode = maxCode.trim();
        if (maxCode.equals("")) {
            //  无子节点
            if (parentId == null) {
                parentId = -1L;
            }
            if (parentId == -1) {
                // 根节点,取新的code值
                rtn = getNextCode("0", length);
            }
            else {
                // 非根节点,取根节点code,合成新的code值
                sql = "select t.code from " + tableName + " t where t.id=?1";
                List<String> codes = dao.findByNativeSql(sql, String.class, parentId);
                rtn = codes.get(0).trim() + getNextCode("0", length);
            }
        }
        else {
            // 存在最大子节点,取当前最大子节点code值,获取下一个code值,重新合成新的code值
            String code = maxCode;
            code = code.substring((int)(code.length()-length),code.length());
            rtn = maxCode.substring(0, (int)(maxCode.length()-length)) + getNextCode(code, length);
        }
        return rtn;
    }

    /*
     * 根据当前code及code长度,获得下一个code值
     * @params 当前code值, code长度
     * @return 下一个code值
     */
    private static String getNextCode(String code, Long length) {

        String rtn = "";
        Long nextCodeL = Long.valueOf(code)+1;
        String nextCode = nextCodeL.toString();
        if (nextCode.length()>length) {
            rtn = nextCode.substring((int)(nextCode.length()-length),nextCode.length());
        }
        else {
            String leftStr = "";
            for(int i=0; i<(length-nextCode.length()); i++) {
                leftStr = "0" + leftStr;
            }
            rtn = leftStr + nextCode;
        }
        return rtn;
    }

    public static void main(String[] args) {
        String abc = "15";
        String next = getNextCode(abc, 5L);
        System.out.println(next);
    }
}
