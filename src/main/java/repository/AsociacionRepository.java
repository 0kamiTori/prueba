package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Asociacion;

@Repository
public interface AsociacionRepository extends JpaRepository<Asociacion, Long> {
}