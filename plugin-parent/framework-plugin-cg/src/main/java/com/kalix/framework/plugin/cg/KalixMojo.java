package com.kalix.framework.plugin.cg;

import com.kalix.framework.plugin.cg.api.IGenerate;
import com.kalix.framework.plugin.cg.impl.*;
import com.thoughtworks.qdox.JavaProjectBuilder;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.compiler.STException;

import java.io.File;
import java.util.Map;

/**
 * Created by sunlf on 2015/9/18.
 *
 * @goal create-all
 * @phase compile
 * @threadSafe
 */
public class KalixMojo extends AbstractBaseKalixMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.docBuilder = new JavaProjectBuilder();
        for (String r : sources) {
            docBuilder.addSourceTree(new File(r));
        }
        // first, find input directory and files it contains
        //if (!inputDir.exists()) {
            //throw new MojoExecutionException("Input directory '" + inputDir.getAbsolutePath() + "' does not exist");
        //}

        //create api code generate
        IGenerate apiGenerate = new ApiGenerateImpl(attributes, inputDir, outputDir);
        apiGenerate.genJavaSource();
        //create entities code generate
        IGenerate entitiesGenerate = new EntitiesGenerateImpl(attributes, inputDir, outputDir);
        entitiesGenerate.genJavaSource();
        //create dao code generate
        IGenerate daoGenerate = new DaoGenerateImpl(attributes, inputDir, outputDir);
        daoGenerate.genJavaSource();
        //create biz code generate
        IGenerate bizGenerate = new BizGenerateImpl(attributes, inputDir, outputDir);
        bizGenerate.genJavaSource();
        //create rest code generate
        IGenerate restGenerate = new RestGenerateImpl(attributes, inputDir, outputDir);
        restGenerate.genJavaSource();
        //create web code generate
        /**
         * 框架修改后不用生成web了
         */
        //IGenerate webGenerate = new WebGenerateImpl(attributes, inputDir, outputDir);
        //webGenerate.genJavaSource();
        //create extjs code generate
        IGenerate extjsGenerate = new ExtjsGenerateImpl(attributes, inputDir, outputDir);
        extjsGenerate.genJavaSource();
        //创建parent pom file
        String karafPath = attributes.get("karafPath");
        String beanName = attributes.get("beanName");
        String artifactIdPrefix = attributes.get("artifactIdPrefix");
        this.inputDir = new File(karafPath + "/data/tmp/cgt/templates");
        this.outputDir = new File(karafPath + "/data/tmp/cgt/"+beanName+"Bean/target/generate");
        File target = new File(outputDir.getAbsolutePath());
        if (!target.exists())
            target.mkdirs();
        this.outputDir = target;
        createParentPom(attributes, inputDir, outputDir);
        createReadme(attributes, inputDir, outputDir);
    }

    private void createParentPom(Map<String, String> attributes, File inputDir, File outputDir) {
        File pomFile=null;
        File targetFile=null;
        try {
            pomFile=new File(inputDir,"pom.xml.st");
            targetFile=new File(outputDir,"pom.xml");
            String input = Util.readFile(pomFile, AbstractGenernateImpl.encoding);
            ST stringTemplate;
            stringTemplate = new ST(input);
            if (attributes != null) {
                for (Map.Entry<String, String> attrEntry : attributes.entrySet()) {
                    stringTemplate.add(attrEntry.getKey(), attrEntry.getValue());
                }
            }
            String output = stringTemplate.render();
            Util.writeFile(targetFile, AbstractGenernateImpl.encoding, output);
        } catch (STException e) {
            e.printStackTrace();
        } catch (MojoExecutionException e) {
            e.printStackTrace();
        }
    }

    private void createReadme(Map<String, String> attributes, File inputDir, File outputDir) {
        File pomFile=null;
        File targetFile=null;
        try {
            pomFile=new File(inputDir,"README.md.st");
            targetFile=new File(outputDir,"README.md");
            String input = Util.readFile(pomFile, AbstractGenernateImpl.encoding);
            ST stringTemplate;
            stringTemplate = new ST(input);
            if (attributes != null) {
                for (Map.Entry<String, String> attrEntry : attributes.entrySet()) {
                    stringTemplate.add(attrEntry.getKey(), attrEntry.getValue());
                }
            }
            String output = stringTemplate.render();
            Util.writeFile(targetFile, AbstractGenernateImpl.encoding, output);
        } catch (STException e) {
            e.printStackTrace();
        } catch (MojoExecutionException e) {
            e.printStackTrace();
        }
    }
}
