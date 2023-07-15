package org.springlogginghelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LoggingAnnotationAdvisor implements PointcutAdvisor, MethodInterceptor {

    private final Pointcut pointcut = new AnnotationMatchingPointcut(null, Logging.class);
    private final JSONLogging jsonLogging;
    private final LogDetailExtractor logDetailExtractor;

    public LoggingAnnotationAdvisor(JSONLogging jsonLogging, LogDetailExtractor logDetailExtractor) {
        this.jsonLogging = jsonLogging;
        this.logDetailExtractor = logDetailExtractor;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String requestBody = (String) request.getAttribute("requestBody");
        LogDetails logDetails = logDetailExtractor.fromRequest(invocation.getMethod(), request);
        jsonLogging.requestLogging(logDetails);

        Object result;
        try {
            result = invocation.proceed();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            logDetails = logDetailExtractor.fromResponse(invocation.getMethod(), result, response);
            jsonLogging.responseLogging(logDetails);
        } catch (Throwable error) {
            logDetails = logDetailExtractor.fromThrowable(error);
            jsonLogging.exceptionThrowLogging(logDetails);
            throw error;
        }
        return result;
    }

    @Override
    public boolean isPerInstance() {
        return false;
    }
}
