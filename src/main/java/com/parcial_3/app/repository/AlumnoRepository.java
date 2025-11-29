package com.parcial_3.app.repository;

import com.parcial_3.app.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByNumeroDocumento(String numeroDocumento);
}
