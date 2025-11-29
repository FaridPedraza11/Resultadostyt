package com.parcial_3.app.controller;

import com.parcial_3.app.model.Usuario;
import com.parcial_3.app.model.Alumno;
import com.parcial_3.app.repository.UsuarioRepository;
import com.parcial_3.app.repository.AlumnoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;

    public HomeController(UsuarioRepository usuarioRepository,
                          AlumnoRepository alumnoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
    }

    // ========= RUTA INICIAL =========

    @GetMapping("/")
    public String mostrarPaginaInicio() {
        return "index";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        // Si alguien entra por GET a /login (escribiendo en la barra), lo mandamos al inicio
        return "redirect:/";
    }

    // ========= PROCESAR LOGIN =========

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("tipoUsuario") String tipoUsuario,
            @RequestParam(value = "usuario", required = false) String usuario,
            @RequestParam(value = "contrasena", required = false) String contrasena,
            @RequestParam(value = "identificacion", required = false) String identificacion,
            Model model
    ) {

        // ---- ADMIN / COORDINADOR ----
        if ("ADMIN".equals(tipoUsuario) || "COORDINADOR".equals(tipoUsuario)) {

            if (usuario == null || usuario.isBlank()
                    || contrasena == null || contrasena.isBlank()) {
                model.addAttribute("error", "Debes ingresar usuario y contraseña.");
                return "index";
            }

            Optional<Usuario> encontrado =
                    usuarioRepository.findByUsernameAndRol(usuario, tipoUsuario);

            if (encontrado.isPresent()
                    && contrasena.equals(encontrado.get().getPassword())) {

                model.addAttribute("nombreUsuario", encontrado.get().getUsername());

                if ("ADMIN".equals(tipoUsuario)) {
                    return "redirect:/admin/usuarios";
                } else {
                    return "coordinador-dashboard";
                }

            } else {
                model.addAttribute("error",
                        "Credenciales incorrectas para " + tipoUsuario.toLowerCase() + ".");
                return "index";
            }
        }

        // ---- ALUMNO ----
        else if ("ALUMNO".equals(tipoUsuario)) {

            if (identificacion == null || identificacion.trim().isEmpty()) {
                model.addAttribute("error", "Debes ingresar tu número de identificación.");
                return "index";
            }

            // Buscar el alumno por número de documento
            Optional<Alumno> alumnoOpt =
                    alumnoRepository.findByNumeroDocumento(identificacion);

            if (alumnoOpt.isEmpty()) {
                model.addAttribute("error",
                        "No se encontró un alumno registrado con ese número de identificación.");
                return "index";
            }

            Alumno alumno = alumnoOpt.get();
            // Enviamos el alumno COMPLETO a la vista
            model.addAttribute("alumno", alumno);

            // Mostramos la página de resultados del alumno
            return "alumno-resultado";
        }

        // ---- SIN TIPO ----
        model.addAttribute("error", "Debes seleccionar un tipo de usuario.");
        return "index";
    }
}
