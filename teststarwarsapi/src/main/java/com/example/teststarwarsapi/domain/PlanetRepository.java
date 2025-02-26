package com.example.teststarwarsapi.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PlanetRepository extends CrudRepository<Planet, Long>, QueryByExampleExecutor<Planet> {
    //Esta assinatura não existe por padrão no CrudRepository.
    //Por isso foi preciso criá-la aqui e o SpringData fará todo o resto(consulta)
    Optional<Planet> findByName(String name);
    
    @Override
    <S extends Planet> List<S> findAll(Example<S> example);
}
