package com.kalix.framework.plugin.cg.impl;


import com.kalix.framework.core.util.Assert;
import com.kalix.framework.core.util.ZipUtil;
import com.kalix.framework.plugin.cg.Util;
import com.kalix.framework.plugin.cg.api.IGenerate;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import org.apache.maven.plugin.MojoExecutionException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.compiler.STException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunlf on 2015/9/18.
 */
public abstract class AbstractGenernateImpl implements IGenerate {
    public final static String encoding = "UTF-8";
    public final static String JAVA_SOURCE_PATH = "\\src\\main\\java\\";
    public final static String RESOURCES_SOURCE_PATH = "src\\main\\resources";
    public final static String JS_SOURCE_PATH = "src\\main\\";
    //处理java类的map
    public Map<String, String> fileMap = new HashMap<>();
    protected Map<File, File> files;
    protected Map<String, String> attributes;
    protected File inputDir, outputDir;
    protected String moduleName;

    // arttributes
    protected String karafPath;
    protected String moduleDescription;
    protected String parentArtifactId;
    protected String parentGroupId;
    protected String parentVersion;
    protected String artifactIdPrefix;
    protected String namePrefix;
    protected String projectName;
    protected String packageName;
    protected String pomName;
    protected String beanName;
    protected String tableName;
    protected String beanDir;
    protected String extjsPrefix;

    public AbstractGenernateImpl(Map<String, String> attributes, File inputDir, File outputDir, String moduleName) {
        this.attributes = attributes;
        this.inputDir = inputDir;
        karafPath = attributes.get("karafPath");
        Assert.notNull(karafPath);

        moduleDescription = attributes.get("moduleDescription");
        Assert.notNull(moduleDescription);
        parentArtifactId = attributes.get("parentArtifactId");
        Assert.notNull(parentArtifactId);
        parentVersion = attributes.get("parentVersion");
        Assert.notNull(parentVersion);
        parentGroupId = attributes.get("parentGroupId");
        Assert.notNull(parentGroupId);
        artifactIdPrefix = attributes.get("artifactIdPrefix");
        Assert.notNull(artifactIdPrefix);
        namePrefix = attributes.get("namePrefix");
        Assert.notNull(namePrefix);
        projectName = attributes.get("projectName");
        Assert.notNull(projectName);
        packageName = attributes.get("packageName");
        Assert.notNull(packageName);
        pomName = attributes.get("pomName");
        Assert.notNull(pomName);
        beanName = attributes.get("beanName");
        Assert.notNull(beanName);
        tableName = attributes.get("tableName");
        Assert.notNull(tableName);
        //特殊化：
        beanDir = attributes.get("beanDir");
        this.beanDir = "src/main/java";
        Assert.notNull(beanDir);

        extjsPrefix = attributes.get("extjsPrefix");
        Assert.notNull(extjsPrefix);

        this.inputDir = new File(karafPath + "/data/tmp/cgt/templates");
        this.outputDir = new File(karafPath + "/data/tmp/cgt/"+beanName+"Bean/generate");
        File target = new File(outputDir.getAbsolutePath() + "\\" + artifactIdPrefix + "-" + moduleName);
        if (!target.exists())
            target.mkdirs();
        this.outputDir = target;
        this.moduleName = moduleName;
    }

    @Override
    public void genJavaSource() throws MojoExecutionException {
        beforeGenJavaSource();
        //处理api模板
        Map<File, File> apiFiles = new LinkedHashMap<File, File>();
        // looks like maven may change empty String to null?
        Assert.notNull(moduleName);
        findFiles(apiFiles, new File(inputDir.getAbsolutePath() + "//" + moduleName), outputDir);

        for (Map.Entry<File, File> fileEntry : apiFiles.entrySet()) {
            File inputFile = fileEntry.getKey();
            File outputFile = fileEntry.getValue();
            String input = Util.readFile(inputFile, encoding);
            ST stringTemplate;
            if (outputFile.getName().contains("ViewModel.js")) {
                //重新修改框架后，不需要生成ViewModel了
                continue;
            }
            try {
                stringTemplate = new ST(input);
                if (attributes != null) {
                    for (Map.Entry<String, String> attrEntry : attributes.entrySet()) {
                        stringTemplate.add(attrEntry.getKey(), attrEntry.getValue());
                    }
                }
                String output = stringTemplate.render();
                Util.writeFile(outputFile, encoding, output);
            } catch (STException e) {
                throw new MojoExecutionException("Problem when trying to process input template '"
                        + inputFile.getAbsolutePath() + "': " + e.getMessage(), e);
            }
        }
    }

    //递归确定模板文件以及对应的目标目录文件
    private void findFiles(Map<File, File> result, File inputDir,
                           File outputDir) {
        CharSequence javaChar = "java";
        CharSequence resourceChar = "xml";
        CharSequence jsChar = "js";
        for (File f : inputDir.listFiles()) {
            String name = f.getName();
            if (f.isDirectory()) {
                findFiles(result, f, new File(outputDir, name));
            } else {
                name = name.substring(0, name.length() - Util.inputSuffix.length());
                String fileName = fileMap.get(name);
                //判断是否为java文件
                if (fileName != null) {
                    //处理java类型的文件
                    if (fileName.contains(javaChar)) {
                        File pd = new File(this.outputDir, JAVA_SOURCE_PATH + packageName.replaceAll("\\.", "/"));
                        File javaFile = new File(pd, fileName);
                        result.put(f, javaFile);
                    }
                    //处理资源类型的文件
                    else if (fileName.contains(resourceChar)) {
                        File pd = new File(this.outputDir, RESOURCES_SOURCE_PATH);
                        File resourceFile = new File(pd, fileName);
                        result.put(f, resourceFile);
                    }
                    //处理资源类型的文件
                    else if (fileName.contains(jsChar)) {
                        //js输出路径
                        File pd = new File(this.outputDir, JS_SOURCE_PATH + "webapp\\" + pomName);
                        File jsFile = new File(pd, fileName);
                        result.put(f, jsFile);
                    }
                } else {
                    if (!outputDir.exists()) {
                        outputDir.mkdirs();
                    }
                    result.put(f, new File(outputDir, name));
                }
            }
        }
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    // 获取实体类信息
    public List<JavaField> getClassFields() throws MojoExecutionException {
        List<com.thoughtworks.qdox.model.JavaField> fields = null;
        JavaProjectBuilder builder = new JavaProjectBuilder();
        try {
            builder.addSource(new FileReader(karafPath + "\\data\\tmp\\cgt\\" + beanName + "Bean\\" + beanName + "Bean.java"));
            builder = builder.setEncoding("utf-8");
            JavaClass cls = builder.getClassByName(packageName + ".entities." + beanName + "Bean");
            fields = cls.getFields();//获取所有字段
            System.out.println("bean=" + packageName + ".entities." + beanName + "Bean");
            System.out.println("fields=" + fields.size());
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException("Problem when trying to process beanName'" + "': " + e.getMessage(), e);
        }
        return fields;
    }
}
