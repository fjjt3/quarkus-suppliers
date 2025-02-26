package org.supplier.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.supplier.infrastructure.entity.Supplier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SupplierRepositoryTest {

    @Inject
    SupplierRepository supplierRepository;

    private Supplier supplier1;
    private Supplier supplier2;

    @BeforeEach
    @Transactional
    public void setUp() {
        supplierRepository.deleteAll(); // Limpia la tabla antes de cada prueba
        supplier1 = new Supplier(null, "Supplier A", LocalDate.of(2023, 1, 15));
        supplier2 = new Supplier(null, "Supplier B", LocalDate.of(2023, 3, 20));
        supplierRepository.persist(supplier1);
        supplierRepository.persist(supplier2);
    }

    @Test
    public void testFindSupplierById_ExistingId() {
        Optional<Supplier> supplier = supplierRepository.findSupplierById(supplier1.getId());
        assertTrue(supplier.isPresent());
        assertEquals("Supplier A", supplier.get().getName());
    }

    @Test
    public void testFindSupplierById_NonExistingId() {
        Optional<Supplier> supplier = supplierRepository.findSupplierById(3L);
        assertFalse(supplier.isPresent());
    }

    @Test
    @Transactional
    public void testPersistSupplier() {
        Supplier newSupplier = new Supplier(null, "Supplier C", LocalDate.of(2023, 5, 10));
        supplierRepository.persist(newSupplier);
        assertNotNull(newSupplier.getId()); // Asegura que el ID se haya asignado
        Optional<Supplier> retrievedSupplier = supplierRepository.findSupplierById(newSupplier.getId());
        assertTrue(retrievedSupplier.isPresent());
        assertEquals("Supplier C", retrievedSupplier.get().getName());
    }

    @Test
    public void testGetAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.getAllSuppliers();
        assertEquals(2, suppliers.size());
        assertEquals(supplier1.getName(), suppliers.get(0).getName());
        assertEquals(supplier2.getName(), suppliers.get(1).getName());
    }
}