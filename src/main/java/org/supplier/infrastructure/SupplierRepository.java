package org.supplier.infrastructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.supplier.infrastructure.entity.Supplier;
import java.util.Optional;

@ApplicationScoped
public class SupplierRepository implements PanacheRepository<Supplier> {

    public Optional<Supplier> findSupplierById(Long id) {
        return find("id", id).firstResultOptional();
    }

    // Otros métodos si los necesitas...
}


