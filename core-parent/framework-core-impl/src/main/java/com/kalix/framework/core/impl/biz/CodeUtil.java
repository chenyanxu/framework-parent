package com.kalix.framework.core.impl.biz;

import com.kalix.framework.core.api.dao.IGenericDao;
import com.kalix.framework.core.util.JNDIHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */
public class CodeUtil {

    public static String createCode(Class beanDaoCls, Long parentId, Long length) {

        String rtn = "";
        IGenericDao dao = null;
        try {
            dao = (IGenericDao) JNDIHelper.getJNDIServiceForName(beanDaoCls.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (maxCode.equals("")) {
            if (parentId == -1) {
                rtn = nextCode("0", length);
            }
            else {
                sql = "select t.code from " + tableName + " t where t.id=?1";
                List<String> codes = dao.findByNativeSql(sql, String.class, parentId);
                rtn = codes.get(0) + nextCode("0", length);
            }
        }
        else {
            String code = maxCode;
            code = code.substring((int)(code.length()-length),code.length());
            rtn = maxCode.substring(0, (int)(maxCode.length()-length)) + nextCode(code, length);
        }
        return rtn;
    }

    private static String nextCode(String code, Long length) {

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
        String next = nextCode(abc, 5L);
        System.out.println(next);
    }
}
