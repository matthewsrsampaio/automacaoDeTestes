package com.example.teststarwarsapi.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.example.teststarwarsapi.jacoco.ExcludeFromJacocoGeneratedReport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty()
    @Column(nullable = false, unique = true)
    private String name;

    @NotEmpty()
    @Column(nullable = false)
    private String climate;
    
    @NotEmpty()
    @Column(nullable = false)
    private String terrain;

    public Planet() {}

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String climate, String terrain) {
        this.climate = climate;
        this.terrain = terrain;
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
    public String getClimate() {
        return climate;
    }
    public void setClimate(String climate) {
        this.climate = climate;
    }
    public String getTerrain() {
        return terrain;
    }
    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    //Compara se os objetos têm o mesmo valor em todos os seus campos
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public String toString() {
        return "Planet [id = " + id + ", name = " + name + ", climate = " + climate + ", terrain = " + terrain + "]";
    }

}
