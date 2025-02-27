package org.supplier.infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.supplier.infrastructure.entity.Supplier;
import org.supplier.testData.SupplierTestData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SupplierRepositoryTest {

    @Inject
    SupplierRepository supplierRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        supplierRepository.deleteAll(); // Limpia la tabla antes de cada prueba
        supplierRepository.persist(SupplierTestData.supplier1());
        supplierRepository.persist(SupplierTestData.supplier2());
    }

    @Test
    public void testFindSupplierById_ExistingId() {
        Optional<Supplier> supplier = supplierRepository.findSupplierById(SupplierTestData.supplier1().getId());
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
        Supplier newSupplier = SupplierTestData.supplier3();
        newSupplier.setId(null);
        supplierRepository.persist(newSupplier);
        assertNotNull(newSupplier.getId());
        Optional<Supplier> retrievedSupplier = supplierRepository.findSupplierById(newSupplier.getId());
        assertTrue(retrievedSupplier.isPresent());
        assertEquals("Supplier C", retrievedSupplier.get().getName());
    }

    @Test
    public void testGetAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.getAllSuppliers();
        assertEquals(2, suppliers.size());
        assertEquals(SupplierTestData.supplier1().getName(), suppliers.get(0).getName());
        assertEquals(SupplierTestData.supplier2().getName(), suppliers.get(1).getName());
    }

    @Test
    @Transactional
    public void testCreateSupplier() {
        Supplier newSupplier = SupplierTestData.supplier3();
        newSupplier.setId(null);
        Supplier createdSupplier = supplierRepository.createSupplier(newSupplier);

        assertNotNull(createdSupplier.getId());
        assertEquals("Supplier C", createdSupplier.getName());

        Optional<Supplier> retrievedSupplier = supplierRepository.findSupplierById(createdSupplier.getId());
        assertTrue(retrievedSupplier.isPresent());
        assertEquals("Supplier C", retrievedSupplier.get().getName());
    }

    @Test
    @Transactional
    public void testDeleteSupplierById_ExistingId() {
        boolean deleted = supplierRepository.deleteSupplierById(SupplierTestData.supplier1().getId());
        assertTrue(deleted);

        Optional<Supplier> retrievedSupplier = supplierRepository.findSupplierById(SupplierTestData.supplier1().getId());
        assertFalse(retrievedSupplier.isPresent());
    }

    @Test
    @Transactional
    public void testDeleteSupplierById_NonExistingId() {
        boolean deleted = supplierRepository.deleteSupplierById(3L);
        assertFalse(deleted);
    }

    @Test
    @Transactional
    public void testUpdateSupplier_ExistingId() {
        Supplier supplierToUpdate = SupplierTestData.supplier1();
        supplierToUpdate.setName("Updated Supplier A");
        supplierToUpdate.setStartDate(LocalDate.of(2024, 1, 15));

        Supplier updatedSupplier = supplierRepository.updateSupplier(supplierToUpdate);

        assertEquals("Updated Supplier A", updatedSupplier.getName());
        assertEquals(LocalDate.of(2024, 1, 15), updatedSupplier.getStartDate());

        Optional<Supplier> retrievedSupplier = supplierRepository.findSupplierById(supplierToUpdate.getId());
        assertTrue(retrievedSupplier.isPresent());
        assertEquals("Updated Supplier A", retrievedSupplier.get().getName());
        assertEquals(LocalDate.of(2024, 1, 15), retrievedSupplier.get().getStartDate());
    }

    @Test
    @Transactional
    public void testUpdateSupplier_NonExistingId() {
        Supplier nonExistentSupplier = new Supplier(999L, "Non Existent Supplier", LocalDate.of(2023, 1, 1));

        assertThrows(jakarta.persistence.PersistenceException.class, () -> {
            supplierRepository.updateSupplier(nonExistentSupplier);
        });
    }
}