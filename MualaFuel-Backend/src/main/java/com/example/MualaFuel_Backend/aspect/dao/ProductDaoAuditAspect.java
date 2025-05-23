package com.example.MualaFuel_Backend.aspect.dao;

import com.example.MualaFuel_Backend.entity.Product;
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
public class ProductDaoAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.ProductDao.save(..))",
            returning = "saved")
    public void logSave(JoinPoint jp, Object saved) {
        Product p = (Product) saved;
        auditService.log(
                "PRODUCT_CREATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + p.getId() + ", name=" + p.getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.ProductDao.update(..))",
            returning = "updated")
    public void logUpdate(JoinPoint jp, Object updated) {
        Product p = (Product) updated;
        auditService.log(
                "PRODUCT_UPDATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + p.getId() + ", name=" + p.getName()
        );
    }

    @After("execution(* com.example.MualaFuel_Backend.dao.ProductDao.delete(..)) && args(id)")
    public void logDelete(JoinPoint jp, Long id) {
        auditService.log(
                "PRODUCT_DELETED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + id
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.ProductDao.*(..))",
            throwing = "ex")
    public void logDaoError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "PRODUCT_DAO_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error="
                        + ex.getClass().getSimpleName()
                        + ": " + ex.getMessage()
        );
    }
}
