package com.btgpactual.business.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MinimumAmountNotMetException.class)
    public ResponseEntity<String> handleMinimumAmountNotMetException(MinimumAmountNotMetException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(FundNotFoundException.class)
    public ResponseEntity<String> handleFundNotFoundException(FundNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<String> handleSubscriptionNotFoundException(SubscriptionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CancellationNotAllowedException.class)
    public ResponseEntity<String> handleCancellationNotAllowedException(CancellationNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<String> handleEmailSendException(EmailSendException ex) {
        log.error("Failed to send email notification", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while sending the email notification.");
    }

    @ExceptionHandler(SMSSendException.class)
    public ResponseEntity<String> handleSMSSendException(SMSSendException ex) {
        log.error("Failed to send SMS notification", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while sending the SMS notification.");
    }
}
