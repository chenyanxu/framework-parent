package com.kalix.framework.core.util;

/**
 * Created by Administrator on 2016/7/21.
 */

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 程序实现了ZIP压缩。共分为2部分 ： 压缩（compression）与解压（decompression）
 * <p/>
 * 大致功能包括用了多态，递归等JAVA核心技术，可以对单个文件和任意级联文件夹进行压缩和解压。 需在代码中自定义源输入路径和目标输出路径。
 * <p/>
 * 在本段代码中，实现的是压缩部分；解压部分见本包中Decompression部分。
 *
 * @author HAN
 */

public class ZipUtil {
    private static int k = 1; // 定义递归次数变量

    public ZipUtil() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ZipUtil zipUtil = new ZipUtil();
        try {
            //zipUtil.zip("C:\\Users\\Administrator_\\Desktop\\1.zip",
            //new File("C:\\Users\\Administrator_\\Desktop"));
            zipUtil.unzip("C:\\Users\\Administrator_\\.m2\\repository\\com\\kalix\\framework\\plugin\\framework-plugin-cg\\1.0.0-SNAPSHOT\\framework-plugin-cg-1.0.0-SNAPSHOT.jar", "c:\\test123");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void zip(String zipFileName, File inputFile) throws Exception {
        System.out.println("压缩中...");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        BufferedOutputStream bo = new BufferedOutputStream(out);
        zip(out, inputFile, inputFile.getName(), bo);
        bo.close();
        out.close(); // 输出流关闭
        System.out.println("压缩完成");
    }

    public static void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // 方法重载
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            if (fl.length == 0) {
                out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
                System.out.println(base + "/");
            }
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
            }
            System.out.println("第" + k + "次递归");
            k++;
        } else {
            out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
            System.out.println(base);
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bi = new BufferedInputStream(in);
            int b;
            while ((b = bi.read()) != -1) {
                bo.write(b); // 将字节流写入当前zip目录
            }
            bi.close();
            in.close(); // 输入流关闭
        }
    }

    public static void unzip(String zipFileName, String outputPath) throws Exception {
        try {
            File parent = new File(outputPath);
            if (!parent.exists() && !parent.mkdirs()) {
                throw new Exception("创建解压目录 \"" + parent.getAbsolutePath() + "\" 失败");
            }
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String name = ze.getName();
                if (!ze.isDirectory()) {
                    //parent = new File(child.getParent());
                    File child = new File(parent, name);
                    File tmpPath = new File(child.getParent());
                    //if(child.getParent().contains("templates")) {//只解压templates目录
                    tmpPath.mkdirs();
                    child.createNewFile();
                    FileOutputStream output = new FileOutputStream(child);
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = zis.read(buffer)) > 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                    output.flush();
                    output.close();
                }
                //}
                ze = zis.getNextEntry();
            }
            zis.close();
        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }
    }
}