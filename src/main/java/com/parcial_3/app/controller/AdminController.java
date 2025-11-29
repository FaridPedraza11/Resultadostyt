package com.parcial_3.app.controller;

import com.parcial_3.app.model.Usuario;
import com.parcial_3.app.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;

    public AdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // LISTAR coordinadores + mostrar formulario
    @GetMapping("/usuarios")
    public String listarCoordinadores(Model model,
                                      @RequestParam(value = "id", required = false) Long idEdicion) {

        List<Usuario> coordinadores = usuarioRepository.findByRol("COORDINADOR");
        model.addAttribute("coordinadores", coordinadores);

        Usuario usuarioForm;
        if (idEdicion != null) {
            Optional<Usuario> encontrado = usuarioRepository.findById(idEdicion);
            usuarioForm = encontrado.orElseGet(Usuario::new);
        } else {
            usuarioForm = new Usuario();
        }

        model.addAttribute("usuarioForm", usuarioForm);
        return "admin-usuarios";
    }

    // CREAR o EDITAR coordinador
    @PostMapping("/usuarios/guardar")
    public String guardarCoordinador(@ModelAttribute("usuarioForm") Usuario usuarioForm,
                                     RedirectAttributes redirectAttributes) {

        if (usuarioForm.getId() == null) {
            // Nuevo → rol coordinador
            usuarioForm.setRol("COORDINADOR");
        } else {
            // Edición → no cambiamos el rol
            Usuario existente = usuarioRepository.findById(usuarioForm.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Coordinador no encontrado"));
            existente.setUsername(usuarioForm.getUsername());
            existente.setPassword(usuarioForm.getPassword());
            usuarioForm = existente;
        }

        usuarioRepository.save(usuarioForm);
        redirectAttributes.addFlashAttribute("mensaje", "Coordinador guardado correctamente.");
        return "redirect:/admin/usuarios";
    }

    // ELIMINAR coordinador
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarCoordinador(@PathVariable Long id,
                                      RedirectAttributes redirectAttributes) {
        usuarioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Coordinador eliminado correctamente.");
        return "redirect:/admin/usuarios";
    }

    // Si alguien entra a /admin, lo mandamos a /admin/usuarios
    @GetMapping
    public String redirigirAUsuarios() {
        return "redirect:/admin/usuarios";
    }
}
