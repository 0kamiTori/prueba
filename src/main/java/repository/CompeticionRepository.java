package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Competicion;

@Repository
public interface CompeticionRepository extends JpaRepository<Competicion, Long> {
}