\<?xml version="1.0" encoding="UTF-8"?>
\<!--suppress ALL -->
\<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    \<modelVersion>4.0.0\</modelVersion>

    \<parent>
        \<groupId><packageName>\</groupId>
        \<artifactId><pomName>-parent\</artifactId>
        \<version>1.0.0-SNAPSHOT\</version>
    \</parent>

    \<artifactId><artifactIdPrefix>-biz\</artifactId>
    \<name><namePrefix> Biz\</name>
    \<packaging>bundle\</packaging>

    \<properties>
        \<bundle.symbolicName><packageName>.biz\</bundle.symbolicName>
    \</properties>

    \<dependencies>
        \<dependency>
                \<groupId>com.kalix.framework.core\</groupId>
                \<artifactId>framework-core-impl\</artifactId>
            \</dependency>
        \<dependency>
            \<groupId><packageName>\</groupId>
            \<artifactId><artifactIdPrefix>-api\</artifactId>
        \</dependency>
    \</dependencies>
\</project>