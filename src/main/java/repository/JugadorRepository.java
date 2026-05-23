package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Jugador;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}
