package com.example.teststarwarsapi.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import static common.PlanetConstants.INVALID_PLANET;
import static common.PlanetConstants.PLANET;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = PlanetService.class) //=>Ao usar esta anotação do SpringBoot, o sistema pode se tornar ineficiente ao escalar o projeto.
public class PlanetServiceTest {

    //A nomenclatura no testes seguirão esse padrão:
    //OPERAÇÃO_ESTADO_RETORNO
    //operacao => Quais serão as operações que o método fará?
    //estado => Os parâmetros recebido no método
    //retorno => O retorno esperado

    @Mock
    //@MockBean //=>Dependente do SpringBoot
    private PlanetRepository planetRepository;

    @InjectMocks
    //@Autowired //=>Dependente do SpringBoot
    private PlanetService planetService;

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        //Arrange
        //Quando o planetRespository.save() for chamado exatamente com esse PLANET, então ele vai retornar PLANET.
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        //ACT
        //SUT = System Under Test
        Planet sut = planetService.create(PLANET);

        //ASSERT
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.getPlanetById(1L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnEmpty() {
        when(planetRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.getPlanetById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.getPlanetByName(PLANET.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsEmpty() {
        String fail = "UnexistingName";
        when(planetRepository.findByName(fail)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.getPlanetByName(fail);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
    List<Planet> planets = new ArrayList<>() {
      {
        add(PLANET);
      }
    };
    Example<Planet> query = QueryBuilder.makeQuery(
        new Planet(
            PLANET.getClimate(), 
            PLANET.getTerrain()
        )
    );
    when(planetRepository.findAll(query)).thenReturn(planets);

    List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

    assertThat(sut).isNotEmpty();
    assertThat(sut).hasSize(1);
    assertThat(sut.get(0)).isEqualTo(PLANET);
  }

    @Test
    public void listPlanets_ReturnNoPlanets() {
        when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_doesNotTHrowAnyException() {
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException() {
        doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

        assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
    }

}
