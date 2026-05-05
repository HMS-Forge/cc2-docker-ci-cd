package xyz.vanez.tracker.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_ShouldReturn404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleNotFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).contains("Not found");
    }

    @Test
    void handleValidationExceptions_ShouldReturn400WithErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "name", "Name is required");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<GlobalExceptionHandler.ValidationErrorResponse> response = handler.handleValidationExceptions(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().errors()).containsKey("name");
        assertThat(response.getBody().errors().get("name")).isEqualTo("Name is required");
    }

    @Test
    void handleGeneric_ShouldReturn500() {
        Exception ex = new RuntimeException("Unexpected database error");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleGeneric(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).contains("Внутренняя ошибка сервера");
        assertThat(response.getBody().message()).contains("Unexpected database error");
    }
    @Test
    void errorRecords_ShouldBeCovered() {
        var now = LocalDateTime.now();
        GlobalExceptionHandler.ErrorResponse err = new GlobalExceptionHandler.ErrorResponse(404, "Not found", now);
        assertThat(err.status()).isEqualTo(404);
        assertThat(err.message()).isEqualTo("Not found");
        assertThat(err.timestamp()).isEqualTo(now);

        GlobalExceptionHandler.ValidationErrorResponse valErr = new GlobalExceptionHandler.ValidationErrorResponse(400, "Bad request", Map.of("field", "error"), now);
        assertThat(valErr.status()).isEqualTo(400);
        assertThat(valErr.errors()).containsKey("field");
    }
}