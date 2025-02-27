package org.supplier.infrastructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.supplier.infrastructure.entity.Supplier;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SupplierRepository implements PanacheRepository<Supplier> {

    public Optional<Supplier> findSupplierById(Long id) {
        return find("id", id).firstResultOptional();
    }

    public List<Supplier> getAllSuppliers() {
        return listAll();
    }

    public Supplier createSupplier(Supplier supplier) {
        persist(supplier);
        return supplier;
    }

    public boolean deleteSupplierById(Long id) {
        return deleteById(id);
    }
}


