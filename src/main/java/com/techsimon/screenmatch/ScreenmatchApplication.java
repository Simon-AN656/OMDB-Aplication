package com.techsimon.screenmatch;

import com.techsimon.screenmatch.model.DAtosTemporadas;
import com.techsimon.screenmatch.model.DatosEpisodes;
import com.techsimon.screenmatch.model.DatosSerie;
import com.techsimon.screenmatch.principal.EjemploStrings;
import com.techsimon.screenmatch.principal.Principal;
import com.techsimon.screenmatch.service.ConsumoApi;
import com.techsimon.screenmatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.muetraMenu();

		/*EjemploStrings ejemploStrings = new EjemploStrings();
		ejemploStrings.muestraEjemplo();*/
	}
}
