package org.supplier.interfaces.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SupplierExceptionHandler implements ExceptionMapper<SupplierNotFoundException> {

    @Override
    public Response toResponse(SupplierNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(exception.getMessage()))
                .build();
    }
}
