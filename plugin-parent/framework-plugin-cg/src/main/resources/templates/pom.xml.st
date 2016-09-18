\<?xml version="1.0" encoding="UTF-8"?>
\<!--suppress ALL -->
\<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    \<modelVersion>4.0.0\</modelVersion>

    \<parent>
        \<groupId><parentGroupId>\</groupId>
        \<artifactId><parentArtifactId>\</artifactId>
        \<version><parentVersion>\</version>
    \</parent>

    \<groupId><packageName>\</groupId>
    \<artifactId><pomName>-parent\</artifactId>
    \<name><namePrefix> Parent\</name>
    \<packaging>pom\</packaging>
    \<modules>
        \<module><artifactIdPrefix>-api\</module>
        \<module><artifactIdPrefix>-biz\</module>
        \<module><artifactIdPrefix>-dao\</module>
        \<module><artifactIdPrefix>-entities\</module>
        \<module><artifactIdPrefix>-extjs\</module>
        \<module><artifactIdPrefix>-rest\</module>
    \</modules>

    \<dependencyManagement>
        \<dependencies>
            \<dependency>
                \<groupId>com.kalix.admin.core\</groupId>
                \<artifactId>admin-core-entities\</artifactId>
                \<version>${globle.version}\</version>
            \</dependency>

            \<dependency>
                \<groupId><packageName>\</groupId>
                \<artifactId><artifactIdPrefix>-api\</artifactId>
                \<version>${globle.version}\</version>
            \</dependency>
            \<dependency>
                \<groupId><packageName>\</groupId>
                \<artifactId><artifactIdPrefix>-entities\</artifactId>
                \<version>${globle.version}\</version>
            \</dependency>
        \</dependencies>
    \</dependencyManagement>
\</project>