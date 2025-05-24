package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class AuthControllerAuditAspect {
    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.controller.AuthController.register(..)) && args(registerRequest)",
            returning = "response"
    )
    public void logRegisterSuccess(JoinPoint jp, Object registerRequest, Object response) {
        ResponseEntity<?> resp = (ResponseEntity<?>) response;
        auditService.log(
                "USER_REGISTERED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "request=" + registerRequest + ", status=" + resp.getStatusCode()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.controller.AuthController.register(..))",
            throwing = "ex"
    )
    public void logRegisterFailure(JoinPoint jp, Throwable ex) {
        auditService.log(
                "USER_REGISTER_FAILED",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "error=" + ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.controller.AuthController.login(..)) && args(loginRequest, ..)",
            returning = "response"
    )
    public void logLoginSuccess(JoinPoint jp, Object loginRequest, Object response) {
        ResponseEntity<?> resp = (ResponseEntity<?>) response;
        auditService.log(
                "USER_LOGIN_SUCCESS",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "request=" + loginRequest + ", status=" + resp.getStatusCode()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.controller.AuthController.login(..))",
            throwing = "ex"
    )
    public void logLoginFailure(JoinPoint jp, Throwable ex) {
        String username = "unknown";
        Object[] args = jp.getArgs();
        if (args.length > 0) username = args[0].toString();
        auditService.log(
                "USER_LOGIN_FAILED",
                AuditLevel.WARN,
                jp.getSignature().toShortString(),
                "username=" + username + ", error=" + ex.getClass().getSimpleName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.controller.AuthController.logout(..))"
    )
    public void logLogout(JoinPoint jp) {
        auditService.log(
                "USER_LOGOUT",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                ""
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.controller.AuthController.check(..)) && args(request)",
            returning = "response"
    )
    public void logCheck(JoinPoint jp, Object request, Object response) {
        ResponseEntity<?> resp = (ResponseEntity<?>) response;
        auditService.log(
                "JWT_CHECK",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "status=" + resp.getStatusCode()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.controller.AuthController.check(..))",
            throwing = "ex"
    )
    public void logCheckFailure(JoinPoint jp, Throwable ex) {
        auditService.log(
                "JWT_CHECK_FAILED",
                AuditLevel.WARN,
                jp.getSignature().toShortString(),
                "error=" + ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );
    }
}
