package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    // Hereda todos los métodos: findAll(), save(), findById(), etc.
}
