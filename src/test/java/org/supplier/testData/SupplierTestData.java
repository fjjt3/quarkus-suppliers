package org.supplier.testData;

import org.supplier.infrastructure.entity.Supplier;

import java.time.LocalDate;

public class SupplierTestData {

    public static Supplier supplier1() {
        return new Supplier(1L, "Supplier A", LocalDate.of(2023, 1, 15));
    }

    public static Supplier supplier2() {
        return new Supplier(2L, "Supplier B", LocalDate.of(2023, 3, 20));
    }

    public static Supplier supplier3() {
        return new Supplier(3L, "Supplier C", LocalDate.of(2023, 6, 1));
    }
}