package org.supplier.interfaces;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.supplier.domain.SupplierService;
import org.supplier.infrastructure.entity.Supplier;
import org.supplier.interfaces.exception.SupplierNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/suppliers")
public class SupplierController {

    @Inject
    SupplierService supplierService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Supplier getSupplierById(@PathParam("id") Long id) {
        Optional<Supplier> supplierOptional = supplierService.getSupplierById(id);
        return supplierOptional.orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
    }

    @POST
    public Response createSupplier(Supplier supplier) {
        Supplier createdSupplier = supplierService.createSupplier(supplier);
        return Response.created(URI.create("/suppliers/" + createdSupplier.getId())).entity(createdSupplier).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSupplierById(@PathParam("id") Long id) {
        boolean deleted = supplierService.deleteSupplierById(id);
        if (deleted) {
            return Response.noContent().build(); // 204 No Content
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }
    }


}