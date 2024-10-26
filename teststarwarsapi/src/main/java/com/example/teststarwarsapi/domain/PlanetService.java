package com.example.teststarwarsapi.domain;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PlanetService {
    
    private final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }

    public List<Planet> findAllPlanets() {
        return (List<Planet>) planetRepository.findAll();
    }

    public Planet findPlanetById(Long id) {
        return planetRepository.findById(id).orElse(null);
    }
}
