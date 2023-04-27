package app.credits.aspect;

import app.credits.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(200)
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* app.credits.controller.*.*(..))")
    public Object logControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Started controller's {} endpoint {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("Ended controller's {} endpoint {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());

        return proceed;
    }

    @Around("execution(* app.credits.controllerAdvice.*.*(..))")
    public Object logControllerAdviceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Started controllerAdvice's {} method {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("Ended controllerAdvice's {} method {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());

        return proceed;
    }

    @Around("execution(* app.credits.service.*.*(..))")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Started service's {} method {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        Object proceed = joinPoint.proceed();
        log.info("Ended service's {} method {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());

        return proceed;
    }

    @After("execution(* app.credits.service.EmailService.sendIfSubscribed(..))  && args(user, ..)")
    public void logEmailNotification(User user) {
        if (user.getEmailSubscription()) {
            log.info("Notified user {} on email {}", user.getUsername(), user.getEmail());
        }
    }
}
