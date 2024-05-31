package com.techsimon.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStrings {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Dulce", "Azael", "Fredy", "Jeimy", "Ana", "Simon");

        nombres.stream()
                .sorted()
                .filter(n -> n.startsWith("S"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }
}
