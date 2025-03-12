package com.example.teststarwarsapi.web;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.teststarwarsapi.domain.Planet;
import com.example.teststarwarsapi.domain.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static common.PlanetConstants.PLANET;
import static common.PlanetConstants.PLANETS;
import static common.PlanetConstants.TATOOINE;

//TO run all: mvn clean test verify
//To run only subcutaneos: mvn clean test verify -Dsurefire.skip=true
//To run only testes de unidade: mvn clean verify -DskipITs=true ou mvn clean test

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    final Long planetId = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlanetService planetService;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
        //STUB - expectativa de ação e resposta
        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc
            .perform(post("/planets")
               .content(objectMapper.writeValueAsString(PLANET))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$").value(PLANET)); //Esse cifrão referencia á raiz do JSON (primeira propriedade)
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnBadRequest() throws Exception {
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        mockMvc
            .perform(post("/planets")
               .content(objectMapper.writeValueAsString(emptyPlanet))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isUnprocessableEntity());
        mockMvc
            .perform(post("/planets")
               .content(objectMapper.writeValueAsString(invalidPlanet))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc
            .perform(post("/planets")
               .content(objectMapper.writeValueAsString(PLANET))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
        when(planetService.getPlanetById(1L)).thenReturn(Optional.of(PLANET));

        mockMvc
            .perform(get("/planets/" + planetId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/planets/" + planetId))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        when(planetService.getPlanetByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        mockMvc
            .perform(get("/planets/name/" + PLANET.getName()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanetUnexistingName_ReturnsNotFound() throws Exception {
        mockMvc
            .perform(get("/planets/name/Tatooine"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void listPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

        mockMvc
            .perform(get("/planets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(PLANETS.size())));

        mockMvc
            .perform(get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0]").value(TATOOINE));
    }

    @Test
    public void listPlanet_ReturnsNoPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(Collections.emptyList());

        mockMvc
            .perform(get("/planets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() throws Exception {
        mockMvc
            .perform(delete("/planets/" + planetId))
            .andExpect(status().isNoContent());
    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException() throws Exception {
        doThrow(new EmptyResultDataAccessException(1))
            .when(planetService).remove(planetId);

        mockMvc
            .perform(delete("/planets/" + planetId))
            .andExpect(status().isNotFound());
    }
    
}
