package com.chder.gasheatsupply_cloud.aspect;

import com.chder.gasheatsupply_cloud.utils.ParamTypeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class SysLogAspect {

    /**
     * 切入点：拦截所有加了@SysLog注解的方法
     *
     * @Pointcut("@annotation(com.chder.gasheatsupply.aspect.SysLog)") 或直接拦截指定包下的所有方法（如：execution(* com.example.service.*.*(..))）
     */
    @Pointcut("execution(* com.chder.gasheatsupply_cloud.service..*(..))")
    public void exceptionLogPointcut() {
    }

    /**
     * 异常通知：方法抛出异常后执行
     *
     * @param joinPoint 连接点（包含方法信息、参数等）
     * @param e         抛出的异常对象
     */
    @AfterThrowing(pointcut = "exceptionLogPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        // 1. 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        // 2. 构建日志内容
        StringBuilder logMsg = new StringBuilder();
        logMsg.append("\n===== 异常日志记录 =====")
                .append("\n时间：").append(LocalDateTime.now())
                .append("\n类名：").append(className)
                .append("\n方法：").append(methodName)
                .append("\n参数：").append(buildParamLog(paramNames, paramValues))
                .append("\n异常类型：").append(e.getClass().getName())
                .append("\n异常信息：").append(e.getMessage())
                .append("\n堆栈跟踪：").append(getStackTrace(e))
                .append("\n=======================\n");

        // 3. 记录日志（使用ERROR级别）
        log.error(logMsg.toString());
    }

    /**
     * 构建参数日志（拼接参数名和参数值）
     */
    private String buildParamLog(String[] paramNames, Object[] paramValues) {
        if (paramNames == null || paramNames.length == 0) {
            return "无参数";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder paramLog = new StringBuilder();
        for (int i = 0; i < paramNames.length; i++) {
//            paramLog.append(paramNames[i]).append("=").append(paramValues[i]).append(", ");
            String json = "";
            Object paramValue = paramValues[i];
            json = paramValue.toString();
            // 判断参数类型并处理
            if (ParamTypeUtils.isCustomObject(paramValue)) {
                // 格式化输出（带缩进）
                try {
                    json = objectMapper.writeValueAsString(paramValue);
                } catch (JsonProcessingException ex) {
                    log.error("格式化对象错误：{}", paramValue);
                }
            }
            paramLog.append(paramNames[i]).append("=").append(json).append(", ");
        }
        return paramLog.deleteCharAt(paramLog.length() - 2).toString();
    }

    /**
     * 获取异常堆栈跟踪信息
     */
    private String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}