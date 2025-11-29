package com.parcial_3.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos personales
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombresApellidos;
    private String correo;
    private String telefono;
    private String numeroRegistro; // alfanumérico

    // Puntajes (0 - 300)
    private Integer comunicacionEscrita;
    private Integer razonamientoCuantitativo;
    private Integer lecturaCritica;
    private Integer competenciasCiudadanas;
    private Integer ingles;
    private Integer formulacionProyectosIngenieria;
    private Integer pensamientoCientificoMatematicoEstadistica;
    private Integer disenoSoftware;

    public Alumno() {
    }

    // ===== GETTERS y SETTERS =====

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTipoDocumento() { return tipoDocumento; }

    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }

    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getNombresApellidos() { return nombresApellidos; }

    public void setNombresApellidos(String nombresApellidos) { this.nombresApellidos = nombresApellidos; }

    public String getCorreo() { return correo; }

    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNumeroRegistro() { return numeroRegistro; }

    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public Integer getComunicacionEscrita() { return comunicacionEscrita; }

    public void setComunicacionEscrita(Integer comunicacionEscrita) { this.comunicacionEscrita = comunicacionEscrita; }

    public Integer getRazonamientoCuantitativo() { return razonamientoCuantitativo; }

    public void setRazonamientoCuantitativo(Integer razonamientoCuantitativo) { this.razonamientoCuantitativo = razonamientoCuantitativo; }

    public Integer getLecturaCritica() { return lecturaCritica; }

    public void setLecturaCritica(Integer lecturaCritica) { this.lecturaCritica = lecturaCritica; }

    public Integer getCompetenciasCiudadanas() { return competenciasCiudadanas; }

    public void setCompetenciasCiudadanas(Integer competenciasCiudadanas) { this.competenciasCiudadanas = competenciasCiudadanas; }

    public Integer getIngles() { return ingles; }

    public void setIngles(Integer ingles) { this.ingles = ingles; }

    public Integer getFormulacionProyectosIngenieria() { return formulacionProyectosIngenieria; }

    public void setFormulacionProyectosIngenieria(Integer formulacionProyectosIngenieria) { this.formulacionProyectosIngenieria = formulacionProyectosIngenieria; }

    public Integer getPensamientoCientificoMatematicoEstadistica() { return pensamientoCientificoMatematicoEstadistica; }

    public void setPensamientoCientificoMatematicoEstadistica(Integer pensamientoCientificoMatematicoEstadistica) {
        this.pensamientoCientificoMatematicoEstadistica = pensamientoCientificoMatematicoEstadistica;
    }

    public Integer getDisenoSoftware() { return disenoSoftware; }

    public void setDisenoSoftware(Integer disenoSoftware) { this.disenoSoftware = disenoSoftware; }

    // ===== PROMEDIO GLOBAL (para informes) =====
    @Transient
    public double getPromedioGlobal() {
        int suma = 0;
        int count = 0;

        if (comunicacionEscrita != null) { suma += comunicacionEscrita; count++; }
        if (razonamientoCuantitativo != null) { suma += razonamientoCuantitativo; count++; }
        if (lecturaCritica != null) { suma += lecturaCritica; count++; }
        if (competenciasCiudadanas != null) { suma += competenciasCiudadanas; count++; }
        if (ingles != null) { suma += ingles; count++; }
        if (formulacionProyectosIngenieria != null) { suma += formulacionProyectosIngenieria; count++; }
        if (pensamientoCientificoMatematicoEstadistica != null) { suma += pensamientoCientificoMatematicoEstadistica; count++; }
        if (disenoSoftware != null) { suma += disenoSoftware; count++; }

        return count > 0 ? (double) suma / count : 0.0;
    }

    // ===== NIVELES POR MATERIA =====
    // Regla:
    // 300 - 191 → Nivel 4
    // 190 - 156 → Nivel 3
    // 155 - 126 → Nivel 2
    // 125 -   0 → Nivel 1

    private String calcularNivel(Integer puntaje) {
        if (puntaje == null) {
            return "Sin dato";
        }
        if (puntaje >= 191 && puntaje <= 300) {
            return "Nivel 4";
        } else if (puntaje >= 156 && puntaje <= 190) {
            return "Nivel 3";
        } else if (puntaje >= 126 && puntaje <= 155) {
            return "Nivel 2";
        } else if (puntaje >= 0 && puntaje <= 125) {
            return "Nivel 1";
        } else {
            return "Fuera de rango";
        }
    }

    @Transient
    public String getNivelComunicacionEscrita() {
        return calcularNivel(comunicacionEscrita);
    }

    @Transient
    public String getNivelRazonamientoCuantitativo() {
        return calcularNivel(razonamientoCuantitativo);
    }

    @Transient
    public String getNivelLecturaCritica() {
        return calcularNivel(lecturaCritica);
    }

    @Transient
    public String getNivelCompetenciasCiudadanas() {
        return calcularNivel(competenciasCiudadanas);
    }

    @Transient
    public String getNivelIngles() {
        return calcularNivel(ingles);
    }

    @Transient
    public String getNivelFormulacionProyectosIngenieria() {
        return calcularNivel(formulacionProyectosIngenieria);
    }

    @Transient
    public String getNivelPensamientoCientificoMatematicoEstadistica() {
        return calcularNivel(pensamientoCientificoMatematicoEstadistica);
    }

    @Transient
    public String getNivelDisenoSoftware() {
        return calcularNivel(disenoSoftware);
    }
    // ===== GRADUACIÓN Y BENEFICIOS =====

    /**
     * Un estudiante se puede graduar si su puntaje global es >= 80.
     */
    @Transient
    public boolean isPuedeGraduarse() {
        return getPromedioGlobal() >= 80;
    }

    @Transient
    public String getEstadoGraduacion() {
        return isPuedeGraduarse() ? "Pasó" : "No pasó";
    }

    /**
     * Beneficios según puntaje global:
     * 180 - 210: Exoneración + nota 4.5
     * 211 - 240: Exoneración + nota 4.7 + 50% derechos de grado
     * 241 o más: Exoneración + nota 5.0 + 100% derechos de grado
     */
    @Transient
    public String getBeneficio() {
        double puntaje = getPromedioGlobal();

        if (puntaje >= 180 && puntaje <= 210) {
            return "Exoneración del informe final o Seminario IV con nota 4.5.";
        } else if (puntaje >= 211 && puntaje <= 240) {
            return "Exoneración del informe final o Seminario IV con nota 4.7 y beca del 50% en derechos de grado.";
        } else if (puntaje >= 241) {
            return "Exoneración del informe final o Seminario IV con nota 5.0 y beca del 100% en derechos de grado.";
        } else {
            return "Sin beneficio especial.";
        }
    }

}
