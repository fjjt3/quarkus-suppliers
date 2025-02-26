package org.supplier.interfaces;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.supplier.domain.SupplierService;
import org.supplier.infrastructure.entity.Supplier;
import org.supplier.interfaces.exception.SupplierNotFoundException;

import java.util.Optional;

@Path("/suppliers")
public class SupplierController {

    @Inject
    SupplierService supplierService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Supplier getSupplierById(@PathParam("id") Long id) {
        Optional<Supplier> supplierOptional = supplierService.getSupplierById(id);
        return supplierOptional.orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
    }
}