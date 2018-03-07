package fr.laerce.gestionstages.dao;


import fr.laerce.gestionstages.domain.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
   List<Discipline> findAllByOrderByIdAsc();
   public Discipline findByCode(String code);

}
