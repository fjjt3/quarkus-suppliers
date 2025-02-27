package org.supplier.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.supplier.infrastructure.SupplierRepository;
import org.supplier.infrastructure.entity.Supplier;
import org.supplier.testData.SupplierTestData;

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

    @BeforeEach
    public void setUp() {
        when(supplierRepository.findSupplierById(1L)).thenReturn(Optional.of(SupplierTestData.supplier1()));
        when(supplierRepository.findSupplierById(2L)).thenReturn(Optional.empty());
        when(supplierRepository.getAllSuppliers()).thenReturn(List.of(SupplierTestData.supplier1(), SupplierTestData.supplier2()));
        when(supplierRepository.createSupplier(any(Supplier.class))).thenReturn(SupplierTestData.supplier3());
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
        Supplier result = supplierService.createSupplier(SupplierTestData.supplier3());
        assertEquals(SupplierTestData.supplier3(), result);
        verify(supplierRepository, times(1)).createSupplier(any(Supplier.class));
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