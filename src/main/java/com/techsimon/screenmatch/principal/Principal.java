package com.techsimon.screenmatch.principal;

import com.techsimon.screenmatch.model.DAtosTemporadas;
import com.techsimon.screenmatch.model.DatosEpisodes;
import com.techsimon.screenmatch.model.DatosSerie;
import com.techsimon.screenmatch.model.Episodio;
import com.techsimon.screenmatch.service.ConsumoApi;
import com.techsimon.screenmatch.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=46c89f02";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void  muetraMenu(){
        System.out.println("Por favor escribe el nombre de lam serie que buscas");
        //Busca los datos de la serie
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ","+")+API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de todas las temporadas
        List<DAtosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i < datos.totalTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" +i+ API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DAtosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        //temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los epidosdio para las temporadas
        temporadas.forEach(t -> t.episodes().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las insformaciones a una lista del tipo DatosEspisodio

        List<DatosEpisodes> datosEpisodes = temporadas.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        //Top 5 episodios
        System.out.println("Top 5 episodios");
        datosEpisodes.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodes::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);
        //Convirtiendo los datos a una lista de tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t->t.episodes().stream()
                        .map(d->new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

    }
}
