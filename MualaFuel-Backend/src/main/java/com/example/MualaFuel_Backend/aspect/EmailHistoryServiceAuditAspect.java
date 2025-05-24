package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class EmailHistoryServiceAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.EmailHistoryServiceImpl.saveEmailHistory(..)) && args(entity)",
            argNames = "jp,entity")
    public void logSave(JoinPoint jp, Object entity) {
        auditService.log(
                "EMAIL_HISTORY_SAVED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "recipient=" + entity.toString()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.EmailHistoryServiceImpl.getEmailHistories(..))",
            returning = "page")
    public void logList(JoinPoint jp, Object page) {
        int size = (page instanceof Page<?> p) ? p.getNumberOfElements() : -1;
        auditService.log(
                "EMAIL_HISTORY_LISTED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "returned=" + size + " entries"
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.EmailHistoryServiceImpl.deleteEmailHistory(..)) && args(id)")
    public void logDelete(JoinPoint jp, Long id) {
        auditService.log(
                "EMAIL_HISTORY_DELETED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + id
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.EmailHistoryServiceImpl.*(..))",
            throwing = "ex")
    public void logError(JoinPoint jp, Throwable ex) {
        String args = jp.getArgs().length > 0
                ? java.util.Arrays.stream(jp.getArgs())
                .map(Object::toString).collect(java.util.stream.Collectors.joining(", "))
                : "";
        auditService.log(
                "EMAIL_HISTORY_SERVICE_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error=" + ex.getClass().getSimpleName()
        );
    }
}
