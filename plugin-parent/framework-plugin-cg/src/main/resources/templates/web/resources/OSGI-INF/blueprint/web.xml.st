\<?xml version="1.0" encoding="UTF-8"?>
\<blueprint xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xsi:schemaLocation="http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd">
    \<service interface="com.kalix.framework.core.api.web.IMenu">
        \<bean class="<packageName>.web.impl.<beanName>MenuImpl"/>
    \</service>
\</blueprint>