package com.tmo.tmo_base_jar.conf;

import com.alibaba.fastjson.JSON;
import com.tmo.tmo_base_jar.Log;
import com.tmo.tmo_base_jar.dto.BaseForm;
import com.tmo.tmo_base_jar.dto.BaseResult;
import com.tmo.tmo_base_jar.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by chungjohnny on 17-1-4.
 */
@Configuration
@Component
@Aspect
public class BaseAopConfig {
    private HttpServletRequest httpServletRequest = null;
    private Object form = null;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");


    @Pointcut("execution(* com.tmo..*.*(..)) && @annotation(org.springframework.web.bind.annotation.ResponseBody)")
    public void executeCommonService() {
    }

    @Around("executeCommonService()")
    public String aopAround(ProceedingJoinPoint pjp) {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名

        Map logMap = new HashMap();
        logMap.put("方法", methodName);
        logMap.put("时间", simpleDateFormat.format(new Date(System.currentTimeMillis())));

        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                httpServletRequest = (HttpServletRequest) arg;
            }
            if(arg instanceof HttpServletResponse){

            }
            if (arg instanceof BaseForm) {
                form = arg;
//                logMap.put("参数", form.toString());
            }
            if(arg instanceof String){
//                logMap.put("参数", form.toString());

            }

        }
        String result = null;
        try {
            result = pjp.proceed(args).toString();
        } catch (Throwable throwable) {
            logMap.put("异常", throwable.getLocalizedMessage());
            if (throwable instanceof BusinessException) {
                Map map = JSON.parseObject(throwable.getMessage());
                result = BaseResult.build(map).toJson();
            } else {
                throwable.printStackTrace();
                Map map = new HashMap();
                map.put("code", 9999);
                map.put("message", "未知错误");
                result = BaseResult.build(map, throwable.getLocalizedMessage()).toJson();
            }
        }
        logMap.put("结果", result);
        long end = System.currentTimeMillis();
        logMap.put("耗时", (int)(end-start)+"ms");
        Log.i(JSON.toJSONString(logMap));
        return result;
    }

    @Before("executeCommonService()")
    public void aopBefore() {
    }

    @AfterReturning(returning = "ret", pointcut = "executeCommonService()")
    public void AfterReturning(Object ret) throws Throwable {
    }

    @After("executeCommonService()")
    public void aopAfter() {
    }


}
