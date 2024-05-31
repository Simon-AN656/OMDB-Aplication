package com.techsimon.screenmatch.principal;

import com.techsimon.screenmatch.model.DAtosTemporadas;
import com.techsimon.screenmatch.model.DatosEpisodes;
import com.techsimon.screenmatch.model.DatosSerie;
import com.techsimon.screenmatch.model.Episodio;
import com.techsimon.screenmatch.service.ConsumoApi;
import com.techsimon.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        //temporadas.forEach(t -> t.episodes().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las insformaciones a una lista del tipo DatosEspisodio

        List<DatosEpisodes> datosEpisodes = temporadas.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        //Top 5 episodios
        /*System.out.println("Top 5 episodios");
        datosEpisodes.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e-> System.out.println("Primer filtro (N/A)" + e))
                .sorted(Comparator.comparing(DatosEpisodes::evaluacion).reversed())
                .peek(e -> System.out.println("Segundo ordenacion (M>m" + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Tercer filtro mayusculas" + e))
                .limit(5)
                .forEach(System.out::println);*/


        //Convirtiendo los datos a una lista de tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t->t.episodes().stream()
                        .map(d->new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //Busqueda de espisodios a partir del año de estreno

        /*System.out.println("Indica el año a aprtir del cual deseas ver los episodios");
        var fecha = teclado.nextInt();
        teclado.nextLine();
*/
        //LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        /*episodios.stream()
                .filter(e -> e.getFechaLanzamiento() != null && e.getFechaLanzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada " + e.getTemporada() +
                                "Episodio: " + e.getTitulo() +
                                "Fecha de lanzamiento: " + e.getFechaLanzamiento().format(dtf)
                ));*/

        //Busca epoisodios por pedazo del titulo
        /*System.out.println("Por favor escriba el nombre del episodio que desea ver");
        var pedazoTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            System.out.println("Los datos son: " + episodioBuscado.get());
        }else {
            System.out.println("No encontre nada");
        }*/

        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media de las evaluacionesd: " + est.getAverage());
        System.out.println("Episodio mejor evaluado:" + est.getMax());
        System.out.println("Episodio peor evaluado: " + est.getMin());

    }
}
