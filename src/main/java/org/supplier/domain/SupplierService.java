package org.supplier.domain;

import jakarta.transaction.Transactional;
import org.supplier.infrastructure.SupplierRepository;
import org.supplier.infrastructure.entity.Supplier;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SupplierService {

    @Inject
    SupplierRepository supplierRepository;

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findSupplierById(id);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.getAllSuppliers();
    }

    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.createSupplier(supplier);
    }

    @Transactional
    public boolean deleteSupplierById(Long id) {
        return supplierRepository.deleteSupplierById(id);
    }

    @Transactional
    public Supplier updateSupplier(Supplier supplier) {
        return supplierRepository.updateSupplier(supplier);
    }
}