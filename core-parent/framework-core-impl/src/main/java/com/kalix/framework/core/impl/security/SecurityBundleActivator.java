package com.kalix.framework.core.impl.security;

import com.kalix.framework.core.api.osgi.BaseBundleActivator;
import com.kalix.framework.core.security.DefaultModularRealm;
import com.kalix.framework.core.util.OsgiUtil;
import com.kalix.framework.core.util.StringUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by chenyanxu on 2017/2/28.
 */
public abstract class SecurityBundleActivator extends BaseBundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        DefaultWebSecurityManager webSecurityManager =(DefaultWebSecurityManager) OsgiUtil.waitForServices(WebSecurityManager.class,null);
        String pkg=this.getClass().getName().replace("internal.InitActivator","security");
        String[] pkgSplit=pkg.split("\\.");
        String moduleName=pkgSplit[pkgSplit.length-2];
        String realmName= StringUtils.changeFirstCharacterCase(moduleName,true)+"Realm";
        Realm realm = (Realm) Class.forName(StringUtils.connect(pkg,".",realmName)).newInstance();

        if(webSecurityManager.getRealms()==null) {
            Collection<Realm> realms = new ArrayList();
            realms.add(realm);
            webSecurityManager.setRealms(realms);
        }
        else{
            webSecurityManager.getRealms().add(realm);
        }

        ((DefaultModularRealm)webSecurityManager.getAuthenticator()).getDefinedRealms().put(moduleName,realm);

        super.start(bundleContext);
    }
}
