package com.crudbasico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.Asociacion;
import model.Club;
import model.Competicion;
import model.Entrenador;
import model.Jugador;
import repository.AsociacionRepository;
import repository.ClubRepository;
import repository.CompeticionRepository;
import repository.EntrenadorRepository;
import repository.JugadorRepository;

@Controller
public class QuizController {

    @Autowired
    private ClubRepository clubRepo;
    @Autowired
    private EntrenadorRepository entrenadorRepo;
    @Autowired
    private JugadorRepository jugadorRepo;
    @Autowired
    private AsociacionRepository asociacionRepo;
    @Autowired
    private CompeticionRepository competicionRepo;

    // ==========================================
    // 1. RUTA PRINCIPAL (DASHBOARD)
    // ==========================================
    @GetMapping("/")
    public String index(Model model) {
        // Inicializar datos semilla en PostgreSQL si la base de datos está completamente vacía
        if (entrenadorRepo.count() == 0 && asociacionRepo.count() == 0) {
            insertarDatosSemilla();
        }
        return "index"; // Carga el hub principal (index.html)
    }

    // ==========================================
    // 2. RUTAS PARA CLUBES (Maneja FK 1:1 y N:1)
    // ==========================================
    @GetMapping("/clubes")
    public String viewClubes(Model model) {
        model.addAttribute("clubes", clubRepo.findAll());
        model.addAttribute("entrenadores", entrenadorRepo.findAll()); // Para el select del formulario
        model.addAttribute("asociaciones", asociacionRepo.findAll()); // Para el select del formulario
        return "clubes";
    }

    @PostMapping("/clubes/guardar")
    public String guardarClub(@RequestParam String nombre, 
                              @RequestParam Long idEntrenador, 
                              @RequestParam Long idAsociacion) {
        Club nuevoClub = new Club();
        nuevoClub.setNombre(nombre);
        nuevoClub.setEntrenador(entrenadorRepo.findById(idEntrenador).orElse(null));
        nuevoClub.setAsociacion(asociacionRepo.findById(idAsociacion).orElse(null));
        clubRepo.save(nuevoClub);
        return "redirect:/clubes";
    }

    @PostMapping("/clubes/eliminar")
    public String eliminarClub(@RequestParam Long id) {
        clubRepo.deleteById(id);
        return "redirect:/clubes";
    }

    // ==========================================
    // 3. RUTAS PARA JUGADORES (Maneja FK N:1 a Club)
    // ==========================================
    @GetMapping("/jugadores")
    public String viewJugadores(Model model) {
        model.addAttribute("jugadores", jugadorRepo.findAll());
        model.addAttribute("clubes", clubRepo.findAll()); // <-- CRUCIAL para el <select>
        return "jugadores";
    }

    @PostMapping("/jugadores/guardar")
    public String guardarJugador(@RequestParam String nombre, 
                                 @RequestParam int dorsal, 
                                 @RequestParam String posicion, 
                                 @RequestParam Long idClub) {
        Jugador nuevoJugador = new Jugador();
        nuevoJugador.setNombre(nombre);
        nuevoJugador.setDorsal(dorsal);
        nuevoJugador.setPosicion(posicion);
        nuevoJugador.setClub(clubRepo.findById(idClub).orElse(null)); // Asigna la FK del club
        jugadorRepo.save(nuevoJugador);
        return "redirect:/jugadores";
    }

    @PostMapping("/jugadores/eliminar")
    public String eliminarJugador(@RequestParam Long id) {
        jugadorRepo.deleteById(id);
        return "redirect:/jugadores";
    }

    // ==========================================
    // 4. RUTAS PARA ENTRENADORES (Entidad Independiente)
    // ==========================================
    @GetMapping("/entrenadores")
    public String viewEntrenadores(Model model) {
        model.addAttribute("entrenadores", entrenadorRepo.findAll());
        return "entrenadores";
    }

    @PostMapping("/entrenadores/guardar")
    public String guardarEntrenador(@RequestParam String nombre, 
                                    @RequestParam String apellido,
                                    @RequestParam int edad,
                                    @RequestParam String nacionalidad) {
        Entrenador nuevoEntrenador = new Entrenador();
        nuevoEntrenador.setNombre(nombre);
        nuevoEntrenador.setApellido(apellido);
        nuevoEntrenador.setEdad(edad);
        nuevoEntrenador.setNacionalidad(nacionalidad);
        entrenadorRepo.save(nuevoEntrenador);
        return "redirect:/entrenadores";
    }

    @PostMapping("/entrenadores/eliminar")
    public String eliminarEntrenador(@RequestParam Long id) {
        try {
            entrenadorRepo.deleteById(id);
        } catch (Exception ex) {
            // Captura error si intentas borrar un DT asignado a un club (Restricción FK de Postgres)
            return "redirect:/entrenadores?error=fk_constraint";
        }
        return "redirect:/entrenadores";
    }

    // ==========================================
    // 5. RUTAS PARA ASOCIACIONES (Entidad Independiente)
    // ==========================================
    @GetMapping("/asociaciones")
    public String viewAsociaciones(Model model) {
        model.addAttribute("asociaciones", asociacionRepo.findAll());
        return "asociaciones";
    }

    @PostMapping("/asociaciones/guardar")
    public String guardarAsociacion(@RequestParam String nombre, 
                                    @RequestParam String pais) {
        Asociacion nuevaAsociacion = new Asociacion();
        nuevaAsociacion.setNombre(nombre);
        nuevaAsociacion.setPais(pais);
        asociacionRepo.save(nuevaAsociacion);
        return "redirect:/asociaciones";
    }

    @PostMapping("/asociaciones/eliminar")
    public String eliminarAsociacion(@RequestParam Long id) {
        try {
            asociacionRepo.deleteById(id);
        } catch (Exception ex) {
            return "redirect:/asociaciones?error=fk_constraint";
        }
        return "redirect:/asociaciones";
    }

    // ==========================================
    // 6. RUTAS PARA COMPETICIONES (Entidad Independiente)
    // ==========================================
    @GetMapping("/competiciones")
    public String viewCompeticiones(Model model) {
        model.addAttribute("competiciones", competicionRepo.findAll());
        return "competiciones";
    }

    @PostMapping("/competiciones/guardar")
    public String guardarCompeticion(@RequestParam String nombre, 
                                     @RequestParam String premio) {
        Competicion nuevaCompeticion = new Competicion();
        nuevaCompeticion.setNombre(nombre);
        nuevaCompeticion.setPremio(premio);
        competicionRepo.save(nuevaCompeticion);
        return "redirect:/competiciones";
    }

    @PostMapping("/competiciones/eliminar")
    public String eliminarCompeticion(@RequestParam Long id) {
        competicionRepo.deleteById(id);
        return "redirect:/competiciones";
    }

    // ==========================================
    // DATOS SEMILLA (Para no arrancar en blanco)
    // ==========================================
    private void insertarDatosSemilla() {
        Entrenador e1 = new Entrenador(); e1.setNombre("Alberto"); e1.setApellido("Gamero"); e1.setNacionalidad("Colombiana");
        Entrenador e2 = new Entrenador(); e2.setNombre("Pablo"); e2.setApellido("Peirano"); e2.setNacionalidad("Uruguaya");
        entrenadorRepo.save(e1); entrenadorRepo.save(e2);

        Asociacion a1 = new Asociacion(); a1.setNombre("FCF"); a1.setPais("Colombia");
        Asociacion a2 = new Asociacion(); a2.setNombre("AFA"); a2.setPais("Argentina");
        asociacionRepo.save(a1); asociacionRepo.save(a2);
        
        Club c1 = new Club(); c1.setNombre("Millonarios FC"); c1.setEntrenador(e1); c1.setAsociacion(a1);
        clubRepo.save(c1);
        
        Jugador j1 = new Jugador(); j1.setNombre("Radamel Falcao"); j1.setDorsal(9); j1.setPosicion("Delantero"); j1.setClub(c1);
        jugadorRepo.save(j1);
        
        Competicion comp1 = new Competicion(); comp1.setNombre("Liga BetPlay"); comp1.setPremio("Trofeo Oficial");
        competicionRepo.save(comp1);
    }
}