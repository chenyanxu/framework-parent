\<?xml version="1.0" encoding="UTF-8"?>

\<!-- default-activation="lazy" -->
\<blueprint xmlns:jpa="http://aries.apache.org/xmlns/jpa/v2.0.0"
           xmlns:tx="http://aries.apache.org/xmlns/transactions/v1.2.0"
           default-activation="eager"
           xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0">
    \<jpa:enable/>
    \<tx:enable-annotations/>

    \<bean id="<pomName>BeanDaoImpl" class="<packageName>.dao.<beanName>BeanDaoImpl">
        \<tx:transaction method="*" value="Required"/>
    \</bean>

    \<service interface="<packageName>.api.dao.I<beanName>BeanDao" ref="<pomName>BeanDaoImpl"/>
\</blueprint>

