package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Entrenador;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
}