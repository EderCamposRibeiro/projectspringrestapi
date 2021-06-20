package project.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.api.rest.model.Profession;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long>{

}
