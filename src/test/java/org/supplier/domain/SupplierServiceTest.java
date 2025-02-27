package org.supplier.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.supplier.infrastructure.SupplierRepository;
import org.supplier.infrastructure.entity.Supplier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SupplierServiceTest {

    @Mock
    SupplierRepository supplierRepository;

    @Inject
    SupplierService supplierService;

    private Supplier supplier1;
    private Supplier supplier2;
    private Supplier supplier3;

    @BeforeEach
    public void setUp() {
        supplier1 = new Supplier(1L, "Supplier A", LocalDate.of(2023, 1, 15));
        supplier2 = new Supplier(2L, "Supplier B", LocalDate.of(2023, 3, 20));
        supplier3 = new Supplier(3L, "Supplier C", LocalDate.of(2023, 6, 1));

        when(supplierRepository.findSupplierById(1L)).thenReturn(Optional.of(supplier1));
        when(supplierRepository.findSupplierById(2L)).thenReturn(Optional.empty());
        when(supplierRepository.getAllSuppliers()).thenReturn(List.of(supplier1, supplier2));
        when(supplierRepository.createSupplier(any(Supplier.class))).thenReturn(supplier3);
        when(supplierRepository.deleteSupplierById(1L)).thenReturn(true);
        when(supplierRepository.deleteSupplierById(2L)).thenReturn(false);
    }

    @Test
    public void testGetSupplierById_ExistingId() {
        Optional<Supplier> result = supplierService.getSupplierById(1L);
        assertTrue(result.isPresent());
        assertEquals("Supplier A", result.get().getName());
        verify(supplierRepository, times(1)).findSupplierById(1L);
    }

    @Test
    public void testGetSupplierById_NonExistingId() {
        Optional<Supplier> result = supplierService.getSupplierById(2L);
        assertFalse(result.isPresent());
        verify(supplierRepository, times(1)).findSupplierById(2L);
    }

    @Test
    public void testGetAllSuppliers() {
        List<Supplier> result = supplierService.getAllSuppliers();
        assertEquals(2, result.size());
        assertEquals("Supplier A", result.get(0).getName());
        assertEquals("Supplier B", result.get(1).getName());
        verify(supplierRepository, times(1)).getAllSuppliers();
    }

    @Test
    public void testCreateSupplier() {
        Supplier newSupplier = new Supplier(null, "Supplier C", LocalDate.of(2023, 6, 1));
        Supplier result = supplierService.createSupplier(newSupplier);
        assertEquals(supplier3, result);
        verify(supplierRepository, times(1)).createSupplier(newSupplier);
    }

    @Test
    public void testDeleteSupplierById_ExistingId() {
        boolean result = supplierService.deleteSupplierById(1L);
        assertTrue(result);
        verify(supplierRepository, times(1)).deleteSupplierById(1L);
    }

    @Test
    public void testDeleteSupplierById_NonExistingId() {
        boolean result = supplierService.deleteSupplierById(2L);
        assertFalse(result);
        verify(supplierRepository, times(1)).deleteSupplierById(2L);
    }
}