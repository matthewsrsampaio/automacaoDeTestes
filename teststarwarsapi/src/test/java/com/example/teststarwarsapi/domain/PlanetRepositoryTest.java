package com.example.teststarwarsapi.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import static common.PlanetConstants.PLANET;
import static common.PlanetConstants.PLANETS;
import static common.PlanetConstants.TATOOINE;

//T E S T E S    D E     I M P L E M E N T A Ç Ã O

//TO run all: mvn clean test verify
//To run only subcutaneos: mvn clean test verify -Dsurefire.skip=true
//To run only testes de unidade: mvn clean verify -DskipITs=true ou mvn clean test

// @SpringBootTest(classes = PlanetRepository.class)
@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager; //Interagir com o banco de dados sem ser via repositório

    private static Stream<Arguments> providesInvalidPlanet() {
        return Stream.of(
            Arguments.of(new Planet("", "", "")), // Todos os campos vazios
            Arguments.of(new Planet("Tatooine", "", "")), // Clima e terreno vazios
            Arguments.of(new Planet("", "Arid", "")), // Nome e terreno vazios
            Arguments.of(new Planet("", "", "Desert")), // Nome e clima vazios
            Arguments.of(new Planet("Tatooine", "Arid", "")), // Terreno vazio
            Arguments.of(new Planet("Tatooine", "", "Desert")), // Clima vazio
            Arguments.of(new Planet("", "Arid", "Desert")), // Nome vazio
            Arguments.of(new Planet(null, null, null)), // Todos os campos nulos
            Arguments.of(new Planet("Tatooine", null, null)), // Clima e terreno nulos
            Arguments.of(new Planet(null, "Arid", null)), // Nome e terreno nulos
            Arguments.of(new Planet(null, null, "Desert")), // Nome e clima nulos
            Arguments.of(new Planet("Tatooine", "Arid", null)), // Terreno nulo
            Arguments.of(new Planet("Tatooine", null, "Desert")), // Clima nulo
            Arguments.of(new Planet(null, "Arid", "Desert")) // Nome nulo
            );
    }

    @AfterEach
    public void afterEach() {
        PLANET.setId(null);
    }

    @Test
    public void createPlanet_WithValidData_ReturnPlanet() {
        //criou o planeta
        Planet planet = planetRepository.save(PLANET);

        //Verifica se o planeta foi criado
        Planet sut = testEntityManager.find(Planet.class, PLANET.getId());

        System.out.println(planet);

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());

    }

    @ParameterizedTest
    @MethodSource("providesInvalidPlanet")
    public void createPlanet_WithInvalidData_ThrowsException(Planet planet) {
        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void create_PlanetWithExistingName_ThrowsException() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet); //Desassocio o planeta do contexto de persistência, do monitoramento do hibernate. Porque o hibernate cria um monitoramento ao vivo, quando eu tento recriar o planeta pra realizar o meu teste o hibernate sabendo que aquele planeta ja existe e que tem um ID vai apenas atualizá-lo.
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planetOpt = planetRepository.findById(planet.getId());

        assertThat(planetOpt).isNotEmpty();
        assertThat(planetOpt.get()).isEqualTo(planet);
    }

    @Test
    public void  getPlanet_ByUnexistingId_ReturnsNotFound() {
        Optional<Planet> planetOpt = planetRepository.findById(1L);

        assertThat(planetOpt).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        Optional<Planet> planetOpt = planetRepository.findByName(planet.getName());

        assertThat(planetOpt).isNotEmpty();
        assertThat(planetOpt.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanetUnexistingName_ReturnsNotFound() throws Exception {
        Optional<Planet> planetOpt = planetRepository.findByName("Tatooine");

        assertThat(planetOpt).isEmpty();
    }

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void listPLanets_ReturnsFilteredPlanets() {
        Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

        List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
        List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);

        assertThat(responseWithoutFilters).isNotEmpty();
        assertThat(responseWithoutFilters).hasSize(PLANETS.size());
        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1);
        assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);        
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet());

        List<Planet> response = planetRepository.findAll(query);

        assertThat(response).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        planetRepository.deleteById(planet.getId());

        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
        assertThat(removedPlanet).isNull();
    }

    //Esse método é para testar se o método remove está lançando a exceção EmptyResultDataAccessException}
    //No entanto, o método deleteById() não retorna mais essa exceção, então o teste não faz mais sentido
    // @Test
    // public void removePlanet_WithUnexistingId_ThrowsException() {
    //     assertThatThrownBy(() -> planetRepository.deleteById(1L))
    //         .isInstanceOf(EmptyResultDataAccessException.class);
    // }

}
