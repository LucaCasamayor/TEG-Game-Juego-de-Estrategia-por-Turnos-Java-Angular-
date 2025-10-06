package ar.edu.utn.frc.tup.piii.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {

    private final ControllerExceptionHandler handler = new ControllerExceptionHandler();

    @Test
    void handleGenericException_returnsInternalServerError() {
        Exception ex = new Exception("Something went wrong");

        var response = handler.handleError(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody().getMessage());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleMethodArgumentNotValidException_returnsBadRequest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getMessage()).thenReturn("Validation failed");

        var response = handler.handleError(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("Bad Request", response.getBody().getError());
    }

    @Test
    void handleEntityNotFoundException_returnsNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        var response = handler.handleError(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody().getMessage());
        assertEquals("Not Found", response.getBody().getError());
    }

    @Test
    void handleResponseStatusException_returnsCustomStatus() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");

        var response = handler.handleError(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied", response.getBody().getMessage());
        assertEquals("Forbidden", response.getBody().getError());
    }
}