package com.rice.base.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: TODO
 * @Author: ln
 * @Date: 2019/8/28 15:44
 * @Version: 1.0
 **/
public class ZipCompress {
    /**
     * 目的地zip文件路径+文件名
     **/
    private String zipFileName;
    /**
     * 待压缩文件路径
     **/
    private String sourceFileName;

    public ZipCompress(String zipFileName, String sourceFileName) {
        this.zipFileName = zipFileName;
        this.sourceFileName = sourceFileName;
    }
    public ZipCompress() { }


    public void zip() throws Exception {
        System.out.println("开始压缩...");

        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        File sourceFile = new File(sourceFileName);

        //调用函数
        compress(out, sourceFile, sourceFile.getName());


        out.close();
        System.out.println("压缩完成！");
    }

    public static void compress(ZipOutputStream out, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if(sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();

            //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            if(flist.length==0) {
                System.out.println(base + File.separator);
                out.putNextEntry(new ZipEntry(base + File.separator));
            } else {
                //如果文件夹不为空，则递归调用compress,文件夹中的每一个文件（或文件夹）进行压缩
                for(int i=0; i<flist.length; i++) {
                    compress(out, flist[i], base+File.separator+flist[i].getName());
                }
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len;

            byte[] buf = new byte[1024];
            System.out.println(base);
            while((len=bis.read(buf, 0, 1024)) != -1) {
                out.write(buf, 0, len);
            }
            bis.close();
            fos.close();
        }
    }

    public static void main(String[] args) {
        ZipCompress zipCom = new ZipCompress("D:\\压缩文件包.zip", "D:\\test");
        try {
            zipCom.zip();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}