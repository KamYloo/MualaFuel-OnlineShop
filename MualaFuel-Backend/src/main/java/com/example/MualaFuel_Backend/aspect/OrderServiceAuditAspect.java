package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.dto.OrderDto;
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
public class OrderServiceAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.OrderServiceImpl.placeOrder(..))",
            returning = "dto")
    public void logPlaceOrder(JoinPoint jp, Object dto) {
        OrderDto o = (OrderDto) dto;
        auditService.log(
                "ORDER_PLACED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "orderId=" + o.getId() + ", total=" + o.getTotalAmount()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.OrderServiceImpl.updateStatusOfOrder(..)) && args(orderId)")
    public void logStatusChange(JoinPoint jp, Long orderId) {
        auditService.log(
                "ORDER_STATUS_UPDATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "orderId=" + orderId
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.OrderServiceImpl.cancelOrder(..)) && args(orderId)")
    public void logCancel(JoinPoint jp, Long orderId) {
        auditService.log(
                "ORDER_CANCELLED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "orderId=" + orderId
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.OrderServiceImpl.*(..))",
            throwing = "ex")
    public void logServiceError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "ORDER_SERVICE_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error=" + ex.getClass().getSimpleName()
        );
    }
}
