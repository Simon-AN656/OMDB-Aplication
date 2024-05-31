package com.techsimon.screenmatch.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DAtosTemporadas(@JsonAlias("Season") Integer numero,
                              @JsonAlias("Episodes") List<DatosEpisodes> episodes) {
}
