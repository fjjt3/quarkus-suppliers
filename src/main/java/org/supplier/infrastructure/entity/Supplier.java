package org.supplier.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @Column(name = "start_date") // Nombre corregido en la base de datos (snake_case)
    @JsonProperty("StartDate") // Nombre en JSON (StartDate)
    private LocalDate startDate; // Nombre corregido en Java (camelCase)

    public Supplier() { }

    public Supplier(Long id, String name, LocalDate startDate) { // Corregido el nombre del parámetro
        this.id = id;
        this.name = name;
        this.startDate = startDate; // Corregido el nombre del campo
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate; // Corregido el nombre del método
    }

    public void setStartDate(LocalDate startDate) { // Corregido el nombre del método
        this.startDate = startDate; // Corregido el nombre del campo
    }
}