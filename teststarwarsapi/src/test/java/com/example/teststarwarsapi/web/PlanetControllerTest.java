package com.example.teststarwarsapi.web;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.teststarwarsapi.domain.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static common.PlanetConstants.PLANET;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

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

        mockMvc.perform(post("/planets")
               .content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$").value(PLANET)); //Esse cifrão referencia á raiz do JSON (primeira propriedade)
    }
    
}
