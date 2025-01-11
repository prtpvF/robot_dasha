package diplom.by.robot.exceptions.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import diplom.by.robot.exceptions.CannotDeleteCommentException;
import diplom.by.robot.exceptions.FileExtensionException;
import diplom.by.robot.exceptions.IllegalEntityException;
import diplom.by.robot.exceptions.NonUniqueEntityException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class ErrHandler {

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Object> personAlreadyExistsExceptionHandler(DataIntegrityViolationException e){
            HttpStatus status = BAD_REQUEST;
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<Object> cannotFindPersonExceptionHandler(EntityNotFoundException e){
            HttpStatus status = NOT_FOUND;
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException e) {
            HttpStatus status = BAD_REQUEST;
            log.error(e.getMessage());
            Set<ConstraintViolation<?>> messages = e.getConstraintViolations();
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<?> violation : messages) {
               errorMessages.add(violation.getMessage());
            }
            return new ResponseEntity<>(errorMessages, status);
        }

        @ExceptionHandler(JWTVerificationException.class)
        public ResponseEntity<Object> tokenHasExpiredHandler(JWTVerificationException e){
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(e.getMessage(),status);
        }

        @ExceptionHandler(IllegalEntityException.class)
        public ResponseEntity<Object> illegalEntityExceptionHandler(IllegalEntityException e){
            HttpStatus status = BAD_REQUEST;
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),status);
        }

        @ExceptionHandler(NonUniqueEntityException.class)
        public ResponseEntity<Object> nonUniqueEntityExceptionHandler(NonUniqueEntityException e){
            HttpStatus status = BAD_REQUEST;
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),status);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException e){
            HttpStatus status = BAD_REQUEST;
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),status);
        }

        @ExceptionHandler(FileExtensionException.class)
        public ResponseEntity<Object> fileExtensionExceptionHandler(FileExtensionException e){
            HttpStatus status = BAD_REQUEST;
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),status);
        }

        @ExceptionHandler(CannotDeleteCommentException.class)
        public ResponseEntity<Object> cannotDeleteCommentExceptionHandler(CannotDeleteCommentException e){
            HttpStatus status = BAD_REQUEST;
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),status);
        }
}