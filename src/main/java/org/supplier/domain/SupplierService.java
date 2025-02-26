package org.supplier.domain;

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


}