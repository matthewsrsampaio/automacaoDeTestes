package com.example.teststarwarsapi.jacoco;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcludeFromJacocoGeneratedReport {
    //Esta anotação é usada para excluir métodos de serem contabilizados no relatório de cobertura de código gerado pelo Jacoco.
}
