package com.tmo.tmo_base_jar.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ImageUtil {

    public static String GenerateImage(byte[] imgStr, String imgFilePath) {// 字符串生成图片

        if (imgStr == null) // 图像数据为空
            return "";
        try {
//            byte[] bytes = imgStr.getBytes();
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(imgStr);
            out.flush();
            out.close();
            return imgFilePath;
        } catch (Exception e) {
            return "";
        }
    }


    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }


    private static byte[] buff = new byte[1024];
    private static BufferedInputStream bis = null;
    private static OutputStream os = null;

    public static boolean downImage(String path, HttpServletResponse res) {

        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + path);


        File file = new File(path);
        if (file.exists()) {
            try {
                os = res.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (os != null) {
                    try {
                        os.close();
                        os = null;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                        bis = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("file not exist");
        return false;
    }
}
