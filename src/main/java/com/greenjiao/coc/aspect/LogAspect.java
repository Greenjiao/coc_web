package com.greenjiao.coc.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @Author Yan
 * @Date 2023/06/09 13:40
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    @Pointcut(value = "execution(* com.greenjiao.coc.service.impl..*(..))")
    public void servicePoint() {
    }

    /**
     * 执行情况
     */

    @Around("servicePoint()")
    public Object logAspect(ProceedingJoinPoint pjp) throws Throwable {
        startTime.set(System.currentTimeMillis());
        //使用ServletRequestAttributes请求上下文获取方法更多
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        //使用数组来获取参数
        Object[] array = pjp.getArgs();
        ObjectMapper mapper = new ObjectMapper();
        //执行函数前打印日志
        log.info("调用前：{}：{},传递的参数为：{}", className, methodName, mapper.writeValueAsString(array));
        log.info("URL:{}", request.getRequestURL().toString());
        log.info("IP地址：{}", request.getRemoteAddr());
        //调用整个目标函数执行
        Object obj = pjp.proceed();
        //执行函数后打印日志
        log.info("调用后：{}：{},返回值为：{}", className, methodName, mapper.writeValueAsString(obj));
        log.info("耗时：{}ms", System.currentTimeMillis() - startTime.get());
        return obj;
    }
}
