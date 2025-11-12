package com.luissoy.historicalprices.shared.infrastructure;

import com.luissoy.historicalprices.price.domain.exception.OverlappingPriceException;
import com.luissoy.historicalprices.product.domain.exception.InvalidProductNameException;
import com.luissoy.historicalprices.product.domain.exception.ProductNotFoundException;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.product.infrastructure.rest.RestErrorResponse;
import com.luissoy.historicalprices.shared.infrastructure.rest.ExceptionHandlerController;
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
    void handleExceptionInternal_shouldBuildCustomResponse_throughPublicPath() {
        var ex = new OverlappingPriceException();
        var request = mock(WebRequest.class);

        var response = handler.badRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Domain error", body.getError());
        assertEquals("Overlapping price periods detected", body.getMessage());
    }
}
