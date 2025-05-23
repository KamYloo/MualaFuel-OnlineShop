package com.example.MualaFuel_Backend.aspect.dao;

import com.example.MualaFuel_Backend.entity.Order;
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
public class OrderDaoAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.OrderDao.save(..))",
            returning = "saved")
    public void logSave(JoinPoint jp, Object saved) {
        Order o = (Order) saved;
        auditService.log(
                "ORDER_DAO_SAVED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + o.getId() + ", status=" + o.getStatus()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.OrderDao.delete(..)) && args(id)")
    public void logDelete(JoinPoint jp, Long id) {
        auditService.log(
                "ORDER_DAO_DELETED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + id
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.OrderDao.*(..))",
            throwing = "ex")
    public void logError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "ORDER_DAO_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error=" + ex.getClass().getSimpleName()
        );
    }
}
