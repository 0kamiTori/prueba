package model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_entrenador")
    private Entrenador entrenador;

    @ManyToOne
    @JoinColumn(name = "id_asociacion")
    private Asociacion asociacion;

    @OneToMany(mappedBy = "club")
    private List<Jugador> jugadores;

    @ManyToMany
    @JoinTable(
        name = "club_competicion",
        joinColumns = @JoinColumn(name = "id_club"),
        inverseJoinColumns = @JoinColumn(name = "id_competicion")
    )
    private List<Competicion> competiciones;

    public Club() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }
    public Asociacion getAsociacion() { return asociacion; }
    public void setAsociacion(Asociacion asociacion) { this.asociacion = asociacion; }
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }
    public List<Competicion> getCompeticiones() { return competiciones; }
    public void setCompeticiones(List<Competicion> competiciones) { this.competiciones = competiciones; }
}