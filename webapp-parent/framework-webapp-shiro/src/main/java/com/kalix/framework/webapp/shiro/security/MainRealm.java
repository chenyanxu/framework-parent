package com.kalix.framework.webapp.shiro.security;


import com.google.gson.Gson;
import com.kalix.framework.core.api.PermissionConstant;
import com.kalix.framework.core.api.security.DefaultUserNamePasswordToken;
import com.kalix.framework.core.api.security.IAuthorizingRealm;
import com.kalix.framework.core.api.security.ILoginService;
import com.kalix.framework.core.api.security.model.Audit;
import com.kalix.framework.core.security.authc.ShiroRealm;
import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.OsgiUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Simple shiro realm with only a few hard coded users
 *
 * @author Minto van der Sluis (misl)
 */
public class MainRealm extends ShiroRealm {// AuthorizingRealm implements IAuthorizingRealm {
//    private ILoginService userLoginService;
//    private EventAdmin eventAdmin;
//    // --------------------------------------------------------------------------
//    // Constructors
//    // --------------------------------------------------------------------------
//
//    public MainRealm() {
//        setName("myMemoryRealm");
//        CredentialsMatcher cm = new SimpleCredentialsMatcher();
//        setCredentialsMatcher(cm);
//        setCachingEnabled(true);
//        eventAdmin= OsgiUtil.waitForServices(EventAdmin.class,null);
//        userLoginService=OsgiUtil.waitForServices(ILoginService.class,null);
//    }
//
//    public void setEventAdmin(EventAdmin eventAdmin) {
//        this.eventAdmin = eventAdmin;
//    }
//
//    /**
//     * 授权查询回调函数
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//
//        String userName = (String) principalCollection.getPrimaryPrincipal();
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        try {
//            userLoginService = JNDIHelper.getJNDIServiceForName(ILoginService.class.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        List<String> userPermission = userLoginService.getUserPermission(userName);
//        if (userPermission != null) {
//            for (String permission : userPermission)
//                info.addRole(permission);
////                        .addStringPermission(permission);
//        }
////        info.addStringPermission("app.admin");
//        return info;
//    }
//
//    /**
//     * 认证回调函数, 登录时调用
//     */
//    @Override
//    @Transactional
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken)
//            throws AuthenticationException {
//        try {
//            userLoginService = JNDIHelper.getJNDIServiceForName(ILoginService.class.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        DefaultUserNamePasswordToken token = (DefaultUserNamePasswordToken) authToken;
//        token.setLoginType("admin");
//        String userName = (String) authToken.getPrincipal();
//        char[] password = (char[]) authToken.getCredentials();
//        // 判断验证码
//        Session session = SecurityUtils.getSubject().getSession();
//        String code = (String) session.getAttribute(PermissionConstant.VALIDATE_CODE);
//
//        /*if (token.getCaptcha() == null || !token.getCaptcha().equalsIgnoreCase(code)) {
//            throw new CaptchaException("验证码错误!");
//        }*/
//        Map map = userLoginService.login(userName, String.valueOf(password));
//        if (map != null) {
//            //保存用户信息到session
//            session = SecurityUtils.getSubject().getSession();
//            Map result = (Map) map.get("response");
//
//            session.setAttribute(PermissionConstant.SYS_CURRENT_USER_REAL_NAME, result.get("name"));
//            session.setAttribute(PermissionConstant.SYS_CURRENT_USER_LOGIN_NAME, result.get("user_name"));
//            session.setAttribute(PermissionConstant.SYS_CURRENT_USER_ID,result.get("user_id"));
//            session.setAttribute(PermissionConstant.SYS_CURRENT_USER_ICON,result.get("user_icon"));
//
//            userLoginService.updateUserLoginInfo((Long) result.get("user_id"), session.getHost());
////            发送用户登录的事件
//            Audit audit = new Audit();
//            audit.setAppName("系统应用");
//            audit.setFunName("系统日志");
//            audit.setAction("系统登录");
//            audit.setActor(String.valueOf(result.get("name")));
//            audit.setContent("登录地址：" + session.getHost());
//            postLoginEvent(audit);
//            return new SimpleAuthenticationInfo(userName, password, getName());
//        }
//
//        throw new AuthenticationException();
//
//    }
//
//    /**
//     * 用户登出发送事件
//     *
//     * @param principals
//     */
//    @Override
//    public void onLogout(PrincipalCollection principals) {
//        Session session = SecurityUtils.getSubject().getSession();
//
//        Audit audit = new Audit();
//        audit.setAppName("系统应用");
//        audit.setFunName("系统日志");
//        audit.setAction("系统登出");
//        audit.setActor(String.valueOf(session.getAttribute(PermissionConstant.SYS_CURRENT_USER_REAL_NAME)));
//        audit.setContent("登出地址：" + session.getHost());
//        postLogoutEvent(audit);
//
//        super.onLogout(principals);
//    }
//
//    private void postLogoutEvent(Audit audit) {
//        Gson gson = new Gson();
//        Dictionary properties = new Hashtable();
//        properties.put("message", gson.toJson(audit));
//        Event event = new Event("com/kalix/userlogout", properties);
//        eventAdmin.postEvent(event);
//    }
//
//    private void postLoginEvent(Audit audit) {
//        Gson gson = new Gson();
//        Dictionary properties = new Hashtable();
//        properties.put("message", gson.toJson(audit));
//        Event event = new Event("com/kalix/userlogin", properties);
//        eventAdmin.postEvent(event);
//    }
}