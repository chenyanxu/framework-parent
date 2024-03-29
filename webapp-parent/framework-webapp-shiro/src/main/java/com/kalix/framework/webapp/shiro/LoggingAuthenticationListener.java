/*
 * Copyright 2013 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kalix.framework.webapp.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.ops4j.pax.shiro.cdi.ShiroIni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Harald Wellmann
 *
 */
@ShiroIni
public class LoggingAuthenticationListener implements AuthenticationListener {
    
    private static Logger log = LoggerFactory.getLogger(LoggingAuthenticationListener.class);

    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        log.info("login success for [{}]", token.getPrincipal());
    }

    public void onFailure(AuthenticationToken token, AuthenticationException ae) {
        log.info("login failure for [{}]", token.getPrincipal());
    }

    public void onLogout(PrincipalCollection principals) {
        log.info("logout: [{}]", principals.getPrimaryPrincipal());
    }

}
