package com.clinica_javierprado.cjp_backend.exception;

import com.clinica_javierprado.cjp_backend.dto.AuthErrorResponse;
import com.clinica_javierprado.cjp_backend.dto.MessageResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<AuthErrorResponse> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(AuthErrorResponse.builder()
                        .code("EMAIL_NOT_VERIFIED")
                        .message(ex.getMessage())
                        .email(ex.getEmail())
                        .build());
    }

    @ExceptionHandler(EmailDeliveryException.class)
    public ResponseEntity<MessageResponse> handleEmailDeliveryException(EmailDeliveryException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Datos invalidos.")
                .orElse("Datos invalidos.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageResponse> handleUnreadableMessage() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("El cuerpo de la solicitud contiene datos invalidos."));
    }
}
