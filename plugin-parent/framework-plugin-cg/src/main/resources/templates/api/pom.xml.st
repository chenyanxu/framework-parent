\<?xml version="1.0" encoding="UTF-8"?>
\<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    \<modelVersion>4.0.0\</modelVersion>

    \<parent>
        \<groupId><packageName>\</groupId>
        \<artifactId><pomName>-parent\</artifactId>
        \<version>1.0.0-SNAPSHOT\</version>
    \</parent>

    \<artifactId><artifactIdPrefix>-api\</artifactId>
    \<name><namePrefix> API\</name>
    \<packaging>bundle\</packaging>

    \<properties>
        \<bundle.symbolicName><packageName>.api\</bundle.symbolicName>
    \</properties>

    \<dependencies>
        \<dependency>
            \<groupId><packageName>\</groupId>
            \<artifactId><artifactIdPrefix>-entities\</artifactId>
        \</dependency>
    \</dependencies>
\</project>