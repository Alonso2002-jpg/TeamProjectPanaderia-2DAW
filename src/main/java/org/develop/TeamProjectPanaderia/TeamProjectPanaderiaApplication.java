package org.develop.TeamProjectPanaderia;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TeamProjectPanaderiaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TeamProjectPanaderiaApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println("Servidor Corriendo en puerto 8080");
		System.out.println("Bienvenidos a Tostones Panaderia 🥖​🍞​");
	}
}
