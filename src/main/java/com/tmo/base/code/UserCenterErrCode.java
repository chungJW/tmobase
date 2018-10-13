package com.tmo.base.code;

import java.util.HashMap;
import java.util.Map;

public class UserCenterErrCode {

    public static Map<Integer, String> map = new HashMap();

    public static Integer NOT_EXIST = 50001;
    public static Integer ALREADY_EXIST = 5002;
    public static Integer INVALID_PASSWORD = 5003;


    public static Integer NO_SUCH_GROUP = 60001;

    static{
        map.put(NOT_EXIST, "用户不存在");
        map.put(ALREADY_EXIST, "用户已存在");
        map.put(NO_SUCH_GROUP, "没有这个组");
        map.put(INVALID_PASSWORD, "密码不正确");
    }

}
