package com.example.MualaFuel_Backend.aspect.service;

import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FileStorageAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.FileStorageServiceImpl.saveFile(..))",
            returning = "relativePath")
    public void logSaveFile(JoinPoint jp, Object relativePath) {
        Object[] args = jp.getArgs();
        String directory = args[1].toString();
        auditService.log(
                "FILE_SAVED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "directory=" + directory + ", path=" + relativePath
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.FileStorageServiceImpl.saveFile(..))",
            throwing = "ex")
    public void logSaveFileError(JoinPoint jp, Throwable ex) {
        Object[] args = jp.getArgs();
        String originalFilename = "<unknown>";
        if (args.length > 0 && args[0] != null) {
            try {
                originalFilename = ((org.springframework.web.multipart.MultipartFile) args[0]).getOriginalFilename();
            } catch (Exception ignored) {}
        }
        String directory = args.length > 1 ? args[1].toString() : "<none>";
        auditService.log(
                "FILE_SAVE_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "directory=" + directory
                        + ", file=" + originalFilename
                        + ", error=" + ex.getClass().getSimpleName()
        );
    }
}
