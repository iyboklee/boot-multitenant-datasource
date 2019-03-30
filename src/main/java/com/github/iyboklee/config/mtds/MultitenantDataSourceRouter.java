/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.config.mtds;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
public class MultitenantDataSourceRouter implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Multitenant DataSource Routing Advisor initialized.");
    }

    @Around("execution(public * com.github.iyboklee.core.service..*.*(..)) && @annotation(org.springframework.transaction.annotation.Transactional)")
    public Object processService(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            String methodName = joinPoint.getSignature().getName();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            if (method.getDeclaringClass().isInterface())
                method = joinPoint.getTarget().getClass().getDeclaredMethod(methodName, method.getParameterTypes());

            Transactional transactional = method.getAnnotation(Transactional.class);
            if (transactional != null && transactional.readOnly())
                TenantContextHolder.set(TenantType.SLAVE);

            return joinPoint.proceed();
        } finally {
            TenantContextHolder.clear();
        }
    }

}