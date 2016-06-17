package com.kalix.framework.core.impl.system;

import com.kalix.framework.core.api.IService;
import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.framework.core.api.system.IJndiService;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @类描述：JNDI 系统服务，用于获得业务服务
 * @创建人：sunlf
 * @创建时间：2014-7-3 下午1:01:59
 * @修改人：
 * @修改时间：
 * @修改备注：
 */

public class JndiService implements IJndiService<IBizService> {
    @Override
    public IBizService getOsgiService(IBizService iService) {
        InitialContext ctx;
        IBizService offers = null;
        try {
            ctx = new InitialContext();
            String jndiName = "osgi:service/" + IService.class.getName();
            offers = (IBizService) ctx.lookup(jndiName);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return offers;
    }
}
