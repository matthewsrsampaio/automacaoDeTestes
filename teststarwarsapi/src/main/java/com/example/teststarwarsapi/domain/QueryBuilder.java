package com.example.teststarwarsapi.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class QueryBuilder {

    private QueryBuilder() { 
        //Este construtor privado existe para que a classe passe nos testes de cobertura de código, mas ele não é necessário.
    }

    public static Example<Planet> makeQuery(Planet planet) {
        ExampleMatcher exampleMatcher = ExampleMatcher
            .matchingAll()
            .withIgnoreCase()
            .withIgnoreNullValues();
        return Example.of(planet, exampleMatcher);
    }


    
}
