package com.luissoy.historicalprices.shared.infrastructure.rest;

import com.luissoy.historicalprices.price.domain.exception.OverlappingPriceException;
import com.luissoy.historicalprices.price.domain.exception.PriceNotFoundException;
import com.luissoy.historicalprices.product.domain.exception.InvalidProductDescriptionException;
import com.luissoy.historicalprices.product.domain.exception.InvalidProductNameException;
import com.luissoy.historicalprices.product.domain.exception.ProductNotFoundException;
import com.luissoy.historicalprices.product.infrastructure.rest.RestErrorResponse;
import com.luissoy.historicalprices.shared.domain.exception.NotFoundException;
import com.luissoy.historicalprices.shared.domain.exception.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            ProductNotFoundException.class,
            PriceNotFoundException.class,
            NotFoundException.class
    })
    public ResponseEntity<RestErrorResponse> notFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
    }

    @ExceptionHandler({
            InvalidProductDescriptionException.class,
            InvalidProductNameException.class,
            OverlappingPriceException.class,
            ValidationException.class
    })
    public ResponseEntity<RestErrorResponse> badRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Domain error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error not handled", ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        HttpStatus status = (statusCode instanceof HttpStatus) ? (HttpStatus) statusCode : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = (body instanceof String s) ? s : ex.getMessage();

        return buildResponseGeneric(status, "Spring Exception", message, headers);
    }

    private ResponseEntity<RestErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        RestErrorResponse response = new RestErrorResponse(status.value(), error, message);
        return ResponseEntity.status(status).body(response);
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<Object> buildResponseGeneric(HttpStatus status, String error, String message, HttpHeaders headers) {
        RestErrorResponse response = new RestErrorResponse(status.value(), error, message);
        return (ResponseEntity<Object>) (ResponseEntity<?>) ResponseEntity.status(status).headers(headers).body(response);
    }
}
