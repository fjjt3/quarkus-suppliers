package org.supplier.interfaces;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.supplier.domain.SupplierService;
import org.supplier.infrastructure.entity.Supplier;
import org.supplier.testData.SupplierTestData;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SupplierControllerTest {

    @Mock
    SupplierService supplierService;

    @BeforeEach
    public void setUp() {
        when(supplierService.getSupplierById(1L)).thenReturn(java.util.Optional.of(SupplierTestData.supplier1()));
        when(supplierService.getSupplierById(2L)).thenReturn(java.util.Optional.empty());
        when(supplierService.getAllSuppliers()).thenReturn(java.util.List.of(SupplierTestData.supplier1(), SupplierTestData.supplier2()));
        when(supplierService.createSupplier(any(Supplier.class))).thenReturn(SupplierTestData.supplier3());
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
        Supplier newSupplier = SupplierTestData.supplier3();
        newSupplier.setId(null);

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

    @Test
    public void testUpdateSupplier_ExistingId() {
        Supplier supplierToUpdate = SupplierTestData.supplier1();
        supplierToUpdate.setName("Updated Supplier A");
        supplierToUpdate.setStartDate(LocalDate.of(2024, 1, 15));

        when(supplierService.updateSupplier(supplierToUpdate)).thenReturn(supplierToUpdate);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(supplierToUpdate)
                .when().put("/suppliers/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Updated Supplier A"))
                .body("startDate", is("2024-01-15"));

        verify(supplierService, times(1)).updateSupplier(supplierToUpdate);
    }

    @Test
    public void testUpdateSupplier_NonExistingId() {
        Supplier nonExistentSupplier = new Supplier(999L, "Non Existent Supplier", LocalDate.of(2023, 1, 1));
        when(supplierService.updateSupplier(nonExistentSupplier)).thenThrow(new RuntimeException("Supplier not found"));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(nonExistentSupplier)
                .when().put("/suppliers/999")
                .then()
                .statusCode(404);

        verify(supplierService, times(1)).updateSupplier(nonExistentSupplier);
    }

    @Test
    public void testUpdateSupplier_IdMismatch() {
        Supplier supplierToUpdate = SupplierTestData.supplier1();
        supplierToUpdate.setName("Updated Supplier A");
        supplierToUpdate.setStartDate(LocalDate.of(2024, 1, 15));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(supplierToUpdate)
                .when().put("/suppliers/2") // ID mismatch
                .then()
                .statusCode(400);
    }
}