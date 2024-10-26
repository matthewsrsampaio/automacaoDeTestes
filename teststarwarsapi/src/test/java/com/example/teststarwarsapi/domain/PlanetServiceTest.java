package com.example.teststarwarsapi.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    //@Autowired //=>Dependente do SpringBoot
    private PlanetService planetService;

    @Mock
    //@MockBean //=>Dependente do SpringBoot
    private PlanetRepository planetRepository;

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
    public void createPlanet_WithInvalidData_ThrowsException() {
        //Arrange
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        //Act
        // Planet sut = planetService.create(INVALID_PLANET);

        //Assert
        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

}
