package com.tmo.tmo_base_jar.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tmo.tmo_base_jar.Log;
import com.tmo.tmo_base_jar.code.KaolaErrCode;
import com.tmo.tmo_base_jar.code.UserCenterErrCode;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

public class BaseResult {

    @ApiModelProperty(required = true)
    private int code = 0;

    @ApiModelProperty(required = true)
    private String message;

    @ApiModelProperty(required = true)
    private Object data = null;

    private static Map<Integer, String> map = new HashMap<>();

    public static final Integer SUCCESS = 0;
    public static final Integer FAILURE = -1;

    static{
        map.put(SUCCESS, "成功");
        map.put(FAILURE, "未知错误");
        map.putAll(UserCenterErrCode.map);
        map.putAll(KaolaErrCode.map);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public Map dateInMap(){
        Map map = null;
        if(data instanceof JSONObject){
            map = (Map) data;
        }else {
            map = JSON.parseObject((String) data);
        }
        return map;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String toJson(){
        Map resp = new HashMap();
        resp.put("code", code);
        resp.put("message", message);
        if (data != null) {
            resp.put("data", data);
        } else {
            resp.put("data", "");
        }
        return JSON.toJSONString(resp, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static BaseResult build(int code, String message, Object data){
        return new BaseResult(code, message, data);
    }

    public static BaseResult build(int code){
        return new BaseResult(code, map.get(code), "");
    }

    public static BaseResult build(int code, Object data){
        return new BaseResult(code, map.get(code), data);
    }

    public static BaseResult build(Map errCode){
        Integer code = 0;
        if(errCode.containsKey("code")) {
            code = (Integer) errCode.get("code");
            errCode.remove("code");
        }
        String message = "";
        if(errCode.containsKey("message")) {
            message = (String) errCode.get("message");
            errCode.remove("message");
        }
        return new BaseResult(code, message, errCode);
    }

    public static BaseResult build(String json){
        Map map = JSON.parseObject(json);
        Log.i(map.toString());
        return new BaseResult(((Integer)map.get("code")).intValue(), (String)map.get("message"), map.get("data"));
    }

    public static BaseResult build(Map errorCode, Object data){
        return new BaseResult((Integer)errorCode.get("code"), (String)errorCode.get("message"), data);
    }

    public BaseResult code(int code){
        this.code = code;
        this.message = map.get(code);
        return this;
    }

    public BaseResult message(String message){
        this.message = message;
        return this;
    }

    public BaseResult data(Object data){
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }


}
