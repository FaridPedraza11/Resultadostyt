package com.parcial_3.app.controller;

import com.parcial_3.app.model.Alumno;
import com.parcial_3.app.repository.AlumnoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    private final AlumnoRepository alumnoRepository;

    public CoordinadorController(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    // ========= DASHBOARD DEL COORDINADOR =========
    @GetMapping("/dashboard")
    public String dashboardCoordinador() {
        return "coordinador-dashboard";
    }

    // ========= CRUD DE ALUMNOS =========

    // Listar alumnos + mostrar formulario (nuevo o edición)
    @GetMapping("/alumnos")
    public String gestionarAlumnos(Model model,
                                   @RequestParam(value = "id", required = false) Long idEdicion) {

        List<Alumno> alumnos = alumnoRepository.findAll();
        model.addAttribute("alumnos", alumnos);

        Alumno alumnoForm;
        if (idEdicion != null) {
            Optional<Alumno> encontrado = alumnoRepository.findById(idEdicion);
            alumnoForm = encontrado.orElseGet(Alumno::new);
        } else {
            alumnoForm = new Alumno();
        }

        model.addAttribute("alumnoForm", alumnoForm);

        return "coordinador-alumnos";
    }

    // Guardar alumno (crear o actualizar)
    @PostMapping("/alumnos/guardar")
    public String guardarAlumno(@ModelAttribute("alumnoForm") Alumno alumno,
                                RedirectAttributes redirectAttributes) {

        // Validación sencilla de puntajes (0–300)
        if (!puntajeValido(alumno.getComunicacionEscrita()) ||
            !puntajeValido(alumno.getRazonamientoCuantitativo()) ||
            !puntajeValido(alumno.getLecturaCritica()) ||
            !puntajeValido(alumno.getCompetenciasCiudadanas()) ||
            !puntajeValido(alumno.getIngles()) ||
            !puntajeValido(alumno.getFormulacionProyectosIngenieria()) ||
            !puntajeValido(alumno.getPensamientoCientificoMatematicoEstadistica()) ||
            !puntajeValido(alumno.getDisenoSoftware())) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "Los puntajes deben ser números enteros entre 0 y 300."
            );
            return "redirect:/coordinador/alumnos";
        }

        alumnoRepository.save(alumno);
        redirectAttributes.addFlashAttribute("mensaje", "Alumno guardado correctamente.");
        return "redirect:/coordinador/alumnos";
    }

    // Eliminar alumno
    @GetMapping("/alumnos/eliminar/{id}")
    public String eliminarAlumno(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {

        alumnoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Alumno eliminado correctamente.");
        return "redirect:/coordinador/alumnos";
    }

    // ========= Placeholders para los informes (los llenamos en el siguiente paso) =========

    @GetMapping("/informe-alumnos")
    public String informeAlumnos(Model model) {
        // Traemos todos los alumnos con sus puntajes
        var alumnos = alumnoRepository.findAll();
        model.addAttribute("alumnos", alumnos);
        return "coordinador-informe-alumnos";
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model) {
        var alumnos = alumnoRepository.findAll();
        model.addAttribute("alumnos", alumnos);
        return "coordinador-informe-beneficios";
    }


    // ========= Helper =========
    private boolean puntajeValido(Integer valor) {
        if (valor == null) {
            return true; // permitimos vacío por ahora
        }
        return valor >= 0 && valor <= 300;
    }
}