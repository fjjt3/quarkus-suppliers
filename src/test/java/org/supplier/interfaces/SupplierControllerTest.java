package org.supplier.interfaces;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.supplier.domain.SupplierService;
import org.supplier.infrastructure.entity.Supplier;

import java.time.LocalDate;
import java.util.Optional;

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
}
