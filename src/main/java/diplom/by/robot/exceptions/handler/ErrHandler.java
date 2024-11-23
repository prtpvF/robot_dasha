package diplom.by.robot.exceptions.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ErrHandler {

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Object> personAlreadyExistsExceptionHandler(DataIntegrityViolationException e){
            HttpStatus status = BAD_REQUEST;
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<Object> cannotFindPersonExceptionHandler(EntityNotFoundException e){
            HttpStatus status = NOT_FOUND;
            return new ResponseEntity<>(e.getMessage(), status);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex) {
            HttpStatus status = BAD_REQUEST;
            Set<ConstraintViolation<?>> messages = ex.getConstraintViolations();
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
}