package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class EmailHistoryAuditAspect {
    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.EmailHistoryDao.save(..))",
            returning = "saved")
    public void onSave(JoinPoint jp, Object saved) {
        EmailHistory e = (EmailHistory) saved;
        auditService.log(
                "EMAIL_HISTORY_SAVED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + e.getId()
                        + ", recipient=" + e.getRecipient()
                        + ", subject=" + e.getSubject()
        );
    }

    @After("execution(* com.example.MualaFuel_Backend.dao.EmailHistoryDao.delete(..)) && args(id)")
    public void onDelete(JoinPoint jp, Long id) {
        auditService.log(
                "EMAIL_HISTORY_DELETED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + id
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.EmailHistoryDao.*(..))",
            throwing = "ex")
    public void onDaoError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "EMAIL_HISTORY_DAO_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error=" + ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );
    }
}
