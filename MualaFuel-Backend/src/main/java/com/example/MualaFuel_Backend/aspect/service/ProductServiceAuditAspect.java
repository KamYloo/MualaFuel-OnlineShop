package com.example.MualaFuel_Backend.aspect.service;

import com.example.MualaFuel_Backend.dto.ProductDto;
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
public class ProductServiceAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.ProductServiceImpl.save(..))",
            returning = "dto")
    public void logServiceSave(JoinPoint jp, Object dto) {
        ProductDto p = (ProductDto) dto;
        auditService.log(
                "PRODUCT_SERVICE_SAVED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + p.getId() + ", name=" + p.getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.ProductServiceImpl.update(..))",
            returning = "dto")
    public void logServiceUpdate(JoinPoint jp, Object dto) {
        ProductDto p = (ProductDto) dto;
        auditService.log(
                "PRODUCT_SERVICE_UPDATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + p.getId() + ", name=" + p.getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.ProductServiceImpl.updateImage(..))",
            returning = "dto")
    public void logServiceUpdateImage(JoinPoint jp, Object dto) {
        ProductDto p = (ProductDto) dto;
        auditService.log(
                "PRODUCT_IMAGE_UPDATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + p.getId() + ", imagePath=" + p.getImagePath()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.ProductServiceImpl.*(..))",
            throwing = "ex")
    public void logServiceError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "PRODUCT_SERVICE_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error="
                        + ex.getClass().getSimpleName()
        );
    }
}
