package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.dto.request.ContactRequest;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class EmailServiceAuditAspect {
    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.EmailService.sendOrderConfirmationEmail(..))")
    public void onOrderEmail(JoinPoint jp) {
        Order order = (Order) jp.getArgs()[0];
        auditService.log(
                "EMAIL_SENT_ORDER",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "orderId=" + order.getId()
                        + ", recipient=" + order.getUser().getEmail()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.EmailService.sendContactFormEmail(..))")
    public void onContactEmail(JoinPoint jp) {
        ContactRequest req = (ContactRequest) jp.getArgs()[0];
        auditService.log(
                "EMAIL_SENT_CONTACT",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "name=" + req.getName()
                        + ", subject=" + req.getSubject()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.EmailService.*(..))",
            throwing = "ex")
    public void onServiceError(JoinPoint jp, Throwable ex) {
        auditService.log(
                "EMAIL_SERVICE_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "error=" + ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );
    }
}
