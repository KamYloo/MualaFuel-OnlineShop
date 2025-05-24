package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.dto.CartDto;
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
public class CartServiceAuditAspect {

    private final AuditService auditService;

    @After("execution(* com.example.MualaFuel_Backend.service.impl.CartServiceImpl.addToCart(..)) && args(productId, quantity)")
    public void logAdd(JoinPoint jp, Long productId, int quantity) {
        auditService.log(
                "CART_ITEM_ADDED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "productId=" + productId + ", quantity=" + quantity
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.CartServiceImpl.getCart(..))",
            returning = "cartDto")
    public void logView(JoinPoint jp, Object cartDto) {
        CartDto c = (CartDto) cartDto;
        auditService.log(
                "CART_VIEWED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "size=" + (c.getItems() != null ? c.getItems().size() : 0)
        );
    }

    @After("execution(* com.example.MualaFuel_Backend.service.impl.CartServiceImpl.updateItemQuantity(..)) && args(productId, newQuantity)")
    public void logUpdate(JoinPoint jp, Long productId, int newQuantity) {
        auditService.log(
                "CART_ITEM_UPDATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "productId=" + productId + ", newQuantity=" + newQuantity
        );
    }

    @After("execution(* com.example.MualaFuel_Backend.service.impl.CartServiceImpl.removeItem(..)) && args(productId)")
    public void logRemove(JoinPoint jp, Long productId) {
        auditService.log(
                "CART_ITEM_REMOVED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "productId=" + productId
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.CartServiceImpl.*(..))",
            throwing = "ex")
    public void logError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "CART_SERVICE_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error=" + ex.getClass().getSimpleName()
        );
    }
}
