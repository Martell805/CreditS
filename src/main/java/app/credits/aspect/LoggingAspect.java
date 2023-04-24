package app.credits.aspect;

import app.credits.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(100)
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* app.credits.controller.*.*(..))")
    public Object logControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Endpoint '{}' started", joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("Endpoint '{}' finished", joinPoint.getSignature().getName());

        return proceed;
    }

    @Around("execution(* app.credits.controllerAdvice.*.*(..))")
    public Object logControllerAdviceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Advice '{}' started", joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("Advice '{}' finished", joinPoint.getSignature().getName());

        return proceed;
    }

    @After("execution(* app.credits.service.EmailService.sendEmail(..))  && args(user, ..)")
    public void logEmailNotification(User user) throws Throwable {
        log.info("Notified user {} on email {}", user.getUsername(), user.getEmail());
    }
}
