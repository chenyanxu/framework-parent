package com.kalix.framework.webapp.cg;

/**
 * Created by Administrator on 2016/7/21.
 */

import java.io.*;
import java.util.zip.*;

/**
 * 程序实现了ZIP压缩。共分为2部分 ： 压缩（compression）与解压（decompression）
 * <p/>
 * 大致功能包括用了多态，递归等JAVA核心技术，可以对单个文件和任意级联文件夹进行压缩和解压。 需在代码中自定义源输入路径和目标输出路径。
 * <p/>
 * 在本段代码中，实现的是压缩部分；解压部分见本包中Decompression部分。
 *
 * @author HAN
 */

public class ZipTool {
    private int k = 1; // 定义递归次数变量

    public ZipTool() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ZipTool book = new ZipTool();
        try {
            book.zip("C:\\Users\\Administrator_\\Desktop\\1.zip",
                    new File("C:\\Users\\Administrator_\\Desktop"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void zip(String zipFileName, File inputFile) throws Exception {
        System.out.println("压缩中...");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        BufferedOutputStream bo = new BufferedOutputStream(out);
        zip(out, inputFile, inputFile.getName(), bo);
        bo.close();
        out.close(); // 输出流关闭
        System.out.println("压缩完成");
    }

    public void zip(ZipOutputStream out, File f, String base, BufferedOutputStream bo) throws Exception { // 方法重载
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

    public void unzip(String zipFileName, String outputFile) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFileName));//输入源zip路径
            BufferedInputStream bin = new BufferedInputStream(zin);
            String Parent = outputFile; //输出路径（文件夹目录）
            File fout = null;
            ZipEntry entry;
            try {
                while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {
                    fout = new File(Parent, entry.getName());
                    if (!fout.exists()) {
                        (new File(fout.getParent())).mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(fout);
                    BufferedOutputStream bout = new BufferedOutputStream(out);
                    int b;
                    while ((b = bin.read()) != -1) {
                        bout.write(b);
                    }
                    bout.close();
                    out.close();
                    System.out.println(fout + "解压成功");
                }
                bin.close();
                zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗费时间： " + (endTime - startTime) + " ms");
    }
}