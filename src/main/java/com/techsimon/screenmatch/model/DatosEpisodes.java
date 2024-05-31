package com.techsimon.screenmatch.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.chrono.IsoChronology;
@JsonIgnoreProperties (ignoreUnknown = true)

public record DatosEpisodes(@JsonAlias ("Title") String titulo, @JsonAlias ("Episode")Integer numeroEpidodio,
                            @JsonAlias ("imdbRating") String evaluacion, @JsonAlias ("Released")String fechaLanzamiento) {
}
