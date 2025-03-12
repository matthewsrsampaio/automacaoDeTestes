package com.example.teststarwarsapi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.example.teststarwarsapi.domain.Planet;

import static common.PlanetConstants.PLANET;
import static common.PlanetConstants.PLANETS;
import static common.PlanetConstants.TATOOINE;

//T E S T E S     S U B C U T Â N E O S
//TO run all: mvn clean test verify
//To run only subcutaneos: mvn clean test verify -Dsurefire.skip=true
//To run only testes de unidade: mvn clean verify -DskipITs=true ou mvn clean test

@ActiveProfiles("it")
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/import_planets.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //Cria o banco antes dos testes    
@Sql(scripts = { "/remove_planets.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) //Limpa o banco depois dos testes    
public class PlanetIT {
    @Autowired
    private TestRestTemplate restTemplate;    

    @Test
    public void contextLoads() {
        //Vai testar se o contexto da aplicação está carregando corretamente
    }

    @Test
    public void createPlanet_ReturnCreated() {
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());

    }

    @Test
    public void getPlanet_ReturnsPlanet() {
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanetByName_ReturnsPlanet() {
        ResponseEntity<Planet> sut = restTemplate
            .getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isNotEmpty();
        assertThat(sut.getBody()).isNotNull();
        assertThat(sut.getBody().length).isEqualTo(PLANETS.size());
        assertThat(sut.getBody()).hasSize(PLANETS.size());
    }

    @Test
    public void listPlanets__ByClimate_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate
            .getForEntity("/planets?climate=" + TATOOINE.getClimate(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate
            .getForEntity("/planets?terrain=" + TATOOINE.getTerrain(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void removePlanet_ReturnsNoContent() {
        ResponseEntity<Void> sut = restTemplate
            .exchange("/planets/" + TATOOINE.getId(), HttpMethod.DELETE, null, void.class);

            assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
