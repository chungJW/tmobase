package com.tmo.base.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {

    public static String saveFile(MultipartFile file, String path) {
        System.out.println("file = "+file+"   path " + path);
        String fileName = "";
        File dest = null;
        if (file != null) {
            if (!file.isEmpty()) {

                //TODO 判断文件文件大小
                long size = file.getSize();
//                System.out.println("上传的文件大小是："+size);
                // 获取文件名
                fileName = file.getOriginalFilename();

                // 解决中文问题，liunx下中文路径，图片显示问题

                dest = new File(path+fileName);

                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);
                    fileName = path+fileName;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileName = "";
                }
            }
        }
        return fileName;
    }

    public static String saveImage(MultipartFile file, String imagePath) {
        String fileName = "";
        File dest = null;
        if (file != null) {
            if (!file.isEmpty()) {

                long size = file.getSize();
                System.out.println("upload file Size ："+size);
                // 获取文件名
                fileName = file.getOriginalFilename();
                // 解决中文问题，liunx下中文路径，图片显示问题

                fileName = /*picPath + */ File.separator + DateUtils.currentFormatDate("yyyyMMdd") + File.separator + UUID.randomUUID() + fileName;

                System.out.println("file name = "+fileName);

                dest = new File(imagePath+fileName);

                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);

                } catch (IOException e) {
                    e.printStackTrace();
                    fileName = "";
                }
            }
        }
        return fileName;
    }

}
