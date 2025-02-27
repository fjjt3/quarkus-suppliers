package org.supplier.interfaces.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupplierExceptionTest {

    @Test
    public void testSupplierNotFoundException() {
        String errorMessage = "Supplier not found with id: 123";
        SupplierNotFoundException exception = new SupplierNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testErrorResponse() {
        String errorMessage = "Test error message";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        assertEquals(errorMessage, errorResponse.getMessage());
    }

    @Test
    public void testSupplierExceptionHandler() {
        String errorMessage = "Supplier not found with id: 456";
        SupplierNotFoundException exception = new SupplierNotFoundException(errorMessage);
        SupplierExceptionHandler handler = new SupplierExceptionHandler();
        Response response = handler.toResponse(exception);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        ErrorResponse responseEntity = (ErrorResponse) response.getEntity();
        assertEquals(errorMessage, responseEntity.getMessage());
    }
}
