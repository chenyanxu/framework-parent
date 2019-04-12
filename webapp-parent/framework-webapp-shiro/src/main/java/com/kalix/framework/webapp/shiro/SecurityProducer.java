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

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.ops4j.pax.shiro.cdi.ShiroIni;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Produces a password service CDI bean to be referenced in shiro.ini.
 * 
 * @author Harald Wellmann
 * 
 */
public class SecurityProducer {

    @Produces
    @ShiroIni
    @Named
    public PasswordService passwordService() {
        return new DefaultPasswordService();
    }

}
