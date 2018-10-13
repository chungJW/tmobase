package com.tmo.base;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Log {

    private static Logger logger = null;
    private static Map<String, Logger> map = new HashMap();

    public static Logger getLogger() {

        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
//        String location = "类名："+stacks[2].getClassName() + "\n函数名：" + stacks[2].getMethodName()
//                + "\n文件名：" + stacks[2].getFileName() + "\n行号："
//                + stacks[2].getLineNumber() + "";
//        System.out.println(location);
        String name = stacks[2].getClassName();
        if(map.containsKey(name)){
            return map.get(name);
        }
        logger = Logger.getLogger(name);
        map.put(name, logger);
        return logger;
    }

    public static void i(String string) {
        getLogger().info(string);
    }

}
