\<blueprint xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xsi:schemaLocation="http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
">

    \<reference id="shiroService" interface="com.kalix.framework.core.api.security.IShiroService"/>
    \<reference id="<pomName>BeanDao" interface="<packageName>.api.dao.I<beanName>BeanDao"/>

    \<bean id="<pomName>BeanServiceImpl" class="<packageName>.biz.<beanName>BeanServiceImpl">
        \<property name="dao" ref="<pomName>BeanDao"/>
        \<property name="shiroService" ref="shiroService"/>
    \</bean>

    \<service id="<pomName>BeanService" interface="<packageName>.api.biz.I<beanName>BeanService"
             ref="<pomName>BeanServiceImpl">
    \</service>
\</blueprint>

