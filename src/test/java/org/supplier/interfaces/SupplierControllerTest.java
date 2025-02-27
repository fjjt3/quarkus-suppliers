package org.supplier.interfaces;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
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

    private Supplier supplier1;
    private Supplier supplier2;
    private Supplier supplier3;

    @BeforeEach
    public void setUp() {
        supplier1 = new Supplier(1L, "Supplier A", LocalDate.of(2023, 1, 15));
        supplier2 = new Supplier(2L, "Supplier B", LocalDate.of(2023, 3, 20));
        supplier3 = new Supplier(3L, "Supplier C", LocalDate.of(2023, 6, 1));

        when(supplierService.getSupplierById(1L)).thenReturn(Optional.of(supplier1));
        when(supplierService.getSupplierById(2L)).thenReturn(Optional.empty());
        when(supplierService.getAllSuppliers()).thenReturn(List.of(supplier1, supplier2));
        when(supplierService.createSupplier(any(Supplier.class))).thenReturn(supplier3);
        when(supplierService.deleteSupplierById(1L)).thenReturn(true);
        when(supplierService.deleteSupplierById(2L)).thenReturn(false);
    }

    @Test
    public void testGetSupplierById_ExistingId() {
        RestAssured.given()
                .when().get("/suppliers/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Supplier A"));

        verify(supplierService, times(1)).getSupplierById(1L);
    }

    @Test
    public void testGetSupplierById_NonExistingId() {
        RestAssured.given()
                .when().get("/suppliers/2")
                .then()
                .statusCode(404);

        verify(supplierService, times(1)).getSupplierById(2L);
    }

    @Test
    public void testGetAllSuppliers() {
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

    @Test
    public void testDeleteSupplierById_ExistingId() {
        RestAssured.given()
                .when().delete("/suppliers/1")
                .then()
                .statusCode(204);

        verify(supplierService, times(1)).deleteSupplierById(1L);
    }

    @Test
    public void testDeleteSupplierById_NonExistingId() {
        RestAssured.given()
                .when().delete("/suppliers/2")
                .then()
                .statusCode(404);

        verify(supplierService, times(1)).deleteSupplierById(2L);
    }
}