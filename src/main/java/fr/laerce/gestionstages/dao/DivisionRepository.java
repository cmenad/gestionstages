package fr.laerce.gestionstages.dao;

import fr.laerce.gestionstages.domain.Discipline;
import fr.laerce.gestionstages.domain.Division;
import fr.laerce.gestionstages.domain.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Projet gestionstages
 * Pour LAERCE SAS
 * <p>
 * Créé le  05/02/2018.
 *
 * @author fred
 */
public interface DivisionRepository extends JpaRepository<Division, Long>{
    List<Division> findAllByOrderByIdAsc();
    public Division findByCode(String code);

    @Query("select professeurs from Division where code = :code")
    List<Professeur> findProfesseurByCode(String code);
}
