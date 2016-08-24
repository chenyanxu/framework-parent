package com.kalix.framework.core.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.*;

/**
 *@author the task impl for auto update pom.xml of the etc project
 */
public class ETCAntTask extends Task {
    private String pomDir;
    private String segment=
                    "                                <artifact>\n" +
                    "                                    <file>target/classes/%s</file>\n" +
                    "                                    <type>%s</type>\n" +
                    "                                    <classifier>%s</classifier>\n" +
                    "                                </artifact>\n";
    private String buildContent=
            "<build>\n" +
            "        <plugins>\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-resources-plugin</artifactId>\n" +
            "                <executions>\n" +
            "                    <execution>\n" +
            "                        <id>filter</id>\n" +
            "                        <phase>generate-resources</phase>\n" +
            "                        <goals>\n" +
            "                            <goal>resources</goal>\n" +
            "                        </goals>\n" +
            "                    </execution>\n" +
            "                </executions>\n" +
            "            </plugin>\n" +
            "            <plugin>\n" +
            "                <groupId>org.codehaus.mojo</groupId>\n" +
            "                <artifactId>build-helper-maven-plugin</artifactId>\n" +
            "                <executions>\n" +
            "                    <execution>\n" +
            "                        <id>attach-artifacts</id>\n" +
            "                        <phase>package</phase>\n" +
            "                        <goals>\n" +
            "                            <goal>attach-artifact</goal>\n" +
            "                        </goals>\n" +
            "                        <configuration>\n" +
            "                            <artifacts>\n" +
                                            "%s"+
            "                            </artifacts>\n" +
            "                        </configuration>\n" +
            "                    </execution>\n" +
            "                </executions>\n" +
            "            </plugin>\n" +
            "        </plugins>\n" +
            "    </build>";
    @Override
    public void execute() throws BuildException {
        System.out.println(pomDir);

        try {
            String encoding="UTF-8";
            File file=new File(pomDir+"\\pom.xml");

            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = "";
                String fileContent="";
                while((lineTxt =bufferedReader.readLine()) != null){
                    fileContent+=lineTxt+"\n";
                }
                read.close();

                if(fileContent.indexOf("<build>")>-1){
                    fileContent=fileContent.substring(0,fileContent.indexOf("<build>"));
                    fileContent+="%s\n</project>";
                }
                else{
                    fileContent=fileContent.replace("</project>","    %s\n</project>");
                }

                File root=new File(pomDir+"\\src\\main\\resources");

                File[] fs=root.listFiles();
                String artifacts="";

                for(int i=0;i<fs.length;++i){
                    if(fs[i].isFile()){
                        String[] fileNameSplit=fs[i].getName().split("\\.");

                        artifacts+=String.format(segment,fs[i].getName(),fileNameSplit[1],fileNameSplit[0]);
                    }
                }

                String build=String.format(buildContent,artifacts);
                String pom=String.format(fileContent,build);

                System.out.println(pom);

                FileWriter fw=new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw=new BufferedWriter(fw);

                bw.write(pom);
                bw.flush();
                bw.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        super.execute();
    }

    public void setPomDir(String pomDir){
        this.pomDir=pomDir;
    }
}
