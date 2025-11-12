package com.luissoy.historicalprices.infrastructure.in.rest.exception;

import com.luissoy.historicalprices.domain.price.exception.OverlappingPriceException;
import com.luissoy.historicalprices.domain.product.exception.InvalidProductNameException;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ExceptionHandlerControllerTest {

    private ExceptionHandlerController handler;

    @BeforeEach
    void setUp() {
        handler = new ExceptionHandlerController();
    }

    @Test
    void notFound_shouldReturn404Response() {
        ProductId productId = new ProductId(1L);
        var ex = new ProductNotFoundException(productId);
        var response = handler.notFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals("Not found", body.getError());
    }

    @Test
    void badRequest_shouldReturn400Response() {
        var ex = new InvalidProductNameException();
        var response = handler.badRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Domain error", body.getError());
    }

    @Test
    void handleAllExceptions_shouldReturn500Response() {
        var ex = new RuntimeException("Unexpected error");
        var request = mock(WebRequest.class);

        var response = handler.handleAllExceptions(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("Error not handled", body.getError());
        assertEquals("Unexpected error", body.getMessage());
    }

    @Test
    void handleExceptionInternal_shouldReturnCustomResponse() {
        var ex = new OverlappingPriceException();
        var headers = new HttpHeaders();
        var request = mock(WebRequest.class);

        var response = handler.handleExceptionInternal(ex, "custom body", headers, HttpStatus.CONFLICT, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        var body = (RestErrorResponse) response.getBody();
        assertNotNull(body);
        assertEquals(409, body.getStatus());
        assertEquals("Spring Exception", body.getError());
        assertEquals("custom body", body.getMessage());
    }
}
