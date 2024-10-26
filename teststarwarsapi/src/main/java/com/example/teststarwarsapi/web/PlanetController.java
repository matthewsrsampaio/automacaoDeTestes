package com.example.teststarwarsapi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.teststarwarsapi.domain.Planet;
import com.example.teststarwarsapi.domain.PlanetService;


@RestController
@RequestMapping("/planets")
public class PlanetController {
    
    @Autowired
    private PlanetService planetService;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet) {
        Planet planetCreated = planetService.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping
    public ResponseEntity<List<Planet>> getAll() {
    List<Planet> planets = planetService.findAllPlanets();
    return ResponseEntity.ok(planets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> findPlanetById(@PathVariable Long id) {
        Planet planet = planetService.findPlanetById(id);
        if (planet != null) {
            return ResponseEntity.ok(planet);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }   
    
}
