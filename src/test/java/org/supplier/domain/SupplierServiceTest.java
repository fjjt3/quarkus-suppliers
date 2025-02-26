package org.supplier.domain;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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

    @Test
    public void testGetSupplierById_ExistingId() {
        Supplier supplier = new Supplier(1L, "Supplier A", LocalDate.of(2023, 1, 15));
        when(supplierRepository.findSupplierById(1L)).thenReturn(Optional.of(supplier));

        Optional<Supplier> result = supplierService.getSupplierById(1L);

        assertTrue(result.isPresent());
        assertEquals("Supplier A", result.get().getName());
        verify(supplierRepository, times(1)).findSupplierById(1L);
    }

    @Test
    public void testGetSupplierById_NonExistingId() {
        when(supplierRepository.findSupplierById(2L)).thenReturn(Optional.empty());

        Optional<Supplier> result = supplierService.getSupplierById(2L);

        assertFalse(result.isPresent());
        verify(supplierRepository, times(1)).findSupplierById(2L);
    }

    @Test
    public void testGetAllSuppliers() {
        List<Supplier> suppliers = List.of(
                new Supplier(1L, "Supplier A", LocalDate.of(2023, 1, 15)),
                new Supplier(2L, "Supplier B", LocalDate.of(2023, 3, 20))
        );
        when(supplierRepository.getAllSuppliers()).thenReturn(suppliers);

        List<Supplier> result = supplierService.getAllSuppliers();

        assertEquals(2, result.size());
        assertEquals("Supplier A", result.get(0).getName());
        assertEquals("Supplier B", result.get(1).getName());

        verify(supplierRepository, times(1)).getAllSuppliers();
    }
}