package com.parcial_3.app.config;

import com.parcial_3.app.model.Usuario;
import com.parcial_3.app.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        // Si no hay usuarios en la tabla, creamos admin y coord
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario("admin", "admin123", "ADMIN");
            Usuario coord = new Usuario("coord", "coord123", "COORDINADOR");

            usuarioRepository.save(admin);
            usuarioRepository.save(coord);

            System.out.println(">>> Usuarios iniciales creados en la BD (admin / coord).");
        } else {
            System.out.println(">>> La tabla usuarios ya tiene datos, no se crean usuarios iniciales.");
        }
    }
}
