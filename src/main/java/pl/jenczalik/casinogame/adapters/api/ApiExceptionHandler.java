package pl.jenczalik.casinogame.adapters.api;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.jenczalik.casinogame.domain.model.GameNotFoundException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        final Map<String, Object> body = prepareResponseBody(exception);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException exception) {
        final Map<String, Object> body = prepareResponseBody(exception);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException exception) {
        final Map<String, Object> body = prepareResponseBody(exception);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> prepareResponseBody(Exception e) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "message", e.getMessage());
    }
}
