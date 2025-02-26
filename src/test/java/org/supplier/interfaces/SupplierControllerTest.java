package org.supplier.interfaces;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.supplier.domain.SupplierService;
import org.supplier.infrastructure.entity.Supplier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SupplierControllerTest {

    @Mock
    SupplierService supplierService;

    @Test
    public void testGetSupplierById_ExistingId() {
        Supplier supplier = new Supplier(1L, "Supplier A", LocalDate.of(2023, 1, 15));
        when(supplierService.getSupplierById(1L)).thenReturn(Optional.of(supplier));

        RestAssured.given()
                .when().get("/suppliers/1")
                .then()
                .statusCode(200)
                .body("id", org.hamcrest.Matchers.is(1))
                .body("name", org.hamcrest.Matchers.is("Supplier A"));

        verify(supplierService, times(1)).getSupplierById(1L);
    }

    @Test
    public void testGetSupplierById_NonExistingId() {
        when(supplierService.getSupplierById(2L)).thenReturn(Optional.empty());

        RestAssured.given()
                .when().get("/suppliers/2")
                .then()
                .statusCode(404); // Expecting 404 Not Found now.

        verify(supplierService, times(1)).getSupplierById(2L);
    }

    @Test
    public void testGetAllSuppliers() {
        List<Supplier> suppliers = List.of(
                new Supplier(1L, "Supplier A", LocalDate.of(2023, 1, 15)),
                new Supplier(2L, "Supplier B", LocalDate.of(2023, 3, 20))
        );
        when(supplierService.getAllSuppliers()).thenReturn(suppliers);

        RestAssured.given()
                .when().get("/suppliers")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].name", is("Supplier A"))
                .body("[1].id", is(2))
                .body("[1].name", is("Supplier B"));

        verify(supplierService, times(1)).getAllSuppliers();
    }

    @Test
    public void testCreateSupplier() {
        Supplier newSupplier = new Supplier(null, "Supplier C", LocalDate.of(2023, 6, 1));
        Supplier createdSupplier = new Supplier(3L, "Supplier C", LocalDate.of(2023, 6, 1));

        when(supplierService.createSupplier(any(Supplier.class))).thenReturn(createdSupplier);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newSupplier)
                .when().post("/suppliers")
                .then()
                .statusCode(201)
                .header("Location", "/suppliers/3")
                .body("id", is(3))
                .body("name", is("Supplier C"));

        verify(supplierService, times(1)).createSupplier(any(Supplier.class));
    }
}
