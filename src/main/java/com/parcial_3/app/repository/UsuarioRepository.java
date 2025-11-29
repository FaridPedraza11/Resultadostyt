package com.parcial_3.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parcial_3.app.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameAndRol(String username, String rol);

    // Para listar todos los coordinadores
    List<Usuario> findByRol(String rol);
}