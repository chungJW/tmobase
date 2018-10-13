package com.tmo.tmo_base_jar.code;

import java.util.HashMap;
import java.util.Map;

public class KaolaErrCode {

    public static Map<Integer, String> map = new HashMap();

    public static Integer NO_SUCH_GOOD = 70002;



    static{
        map.put(NO_SUCH_GOOD, "没有此商品");
    }

}
