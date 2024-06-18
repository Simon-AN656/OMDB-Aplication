package com.techsimon.screenmatch.principal;

import com.techsimon.screenmatch.model.*;
import com.techsimon.screenmatch.repository.iSerieRepository;
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
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private iSerieRepository repositorio;

    public Principal(iSerieRepository repository) {
        this.repositorio = repository;
    }

    public void muetraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1.- Buscar series
                    2.- Buscar episodios
                    3.- Mostrar series buscasdas
                                        
                    0.- Salir""";
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }
    }


    //Busca los datos de la serie
    private DatosSerie getDatosSerie() {
        System.out.println("Por favor escribe el nombre de lam serie que buscas");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    //Busca los datos de todas las temporadas
    private void buscarEpisodioPorSerie() {
        DatosSerie datosSerie = getDatosSerie();
        List<DAtosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i < datosSerie.totalTemporadas(); i++) {
            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DAtosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        temporadas.forEach(System.out::println);
    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie  = new Serie(datos);
        repositorio.save(serie);
        System.out.println(datos);

    }

    private void mostrarSeriesBuscadas() {
        List<Serie> series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

}