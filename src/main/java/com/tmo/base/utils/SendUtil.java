package com.tmo.base.utils;

import com.tmo.base.Log;
import com.tmo.base.dto.BaseResult;
import okhttp3.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SendUtil {

    private static final byte[] LOCKER = new byte[0];
    private static SendUtil mInstance;
    private OkHttpClient mOkHttpClient;

    public static final MediaType APPLICATION_JSON
            = MediaType.parse("application/json; charset=utf-8");

    public enum SERVER{
        PAY("pay"), USERCENTER("usercenter"), KAOLA("kaola");
        private String name;
        SERVER(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            super.toString();
            return name;
        }
    }


    private SendUtil() {
        OkHttpClient.Builder ClientBuilder=new okhttp3.OkHttpClient.Builder();
        ClientBuilder.readTimeout(30, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
        mOkHttpClient=ClientBuilder.build();
    }

    public static SendUtil getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new SendUtil();
                }
            }
        }
        return mInstance;
    }

    public String getUrl(SERVER server, String path) throws IOException {
        String result = null;
        StringBuilder url = new StringBuilder();
        url.append("https://wopay.quzitech.com/");
        url.append(server.name);
        if(path.startsWith("/")){
            url.append(path);
        }else{
            url.append('/');
            url.append(path);
        }
        return url.toString();
    }

    public void requestAsync(String url, RequestBody requestBody){
        Request request = new Request.Builder()
                .url(url.toString())
                .post(requestBody)
                .build();
        System.out.println("url = "+url.toString());
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public String request(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url.toString())
                .post(requestBody)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        String resp = response.body().string();
        response.close();
        return resp;
    }




    public void sendAsync(SERVER server, String path, String json) throws IOException {
        String url = getUrl(server, path);
        RequestBody requestBody = getRequestBody(json);
        requestAsync(url, requestBody);
    }

    public void sendAsync(SERVER server, String path, Map<String, String> map) throws IOException {
        String url = getUrl(server, path);
        RequestBody requestBody = getRequestBody(map);
        requestAsync(url, requestBody);
    }

    public String send(SERVER server, String path, String json) throws IOException {
        String url = getUrl(server, path);
        RequestBody requestBody = getRequestBody(json);
        return request(url, requestBody);
    }

    public BaseResult send(SERVER server, String path, Map<String, String> map) throws IOException {
        Log.i("send to {"+server.name+"} withPath {"+path+"} Params {"+map.toString()+"}");
        String url = getUrl(server, path);
        RequestBody requestBody = getRequestBody(map);
        String resp = request(url, requestBody);
        Log.i("resp {"+resp+"}");
        return BaseResult.build(resp);
    }

    public BaseResult httpSend(String url, Map<String, String> map) throws IOException {
        Log.i("send to {"+url+"} Params = " + map);
        RequestBody requestBody = getRequestBody(map);
        String resp = request(url, requestBody);
        Log.i("resp {"+resp+"}");
        return BaseResult.build(resp);
    }

    public RequestBody getRequestBody(String json){
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        return body;
    }

    public RequestBody getRequestBody(Map BodyParams){
        Log.i("BodyParams = " +BodyParams);
        RequestBody body;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if(BodyParams != null){
            Iterator iterator = BodyParams.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                String value = BodyParams.get(key)+"";
                formEncodingBuilder.add(key, value);
            }
        }
        body=formEncodingBuilder.build();
        return body;
    }



}
