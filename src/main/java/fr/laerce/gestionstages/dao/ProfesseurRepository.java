package fr.laerce.gestionstages.dao;

import fr.laerce.gestionstages.domain.Division;
import fr.laerce.gestionstages.domain.Niveau;
import fr.laerce.gestionstages.domain.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Projet gestionstages
 * Pour LAERCE SAS
 * <p>
 * Créé le  06/02/2018.
 *
 * @author fred
 */
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    public Professeur findByCodeSynchro(String code);

    @Query("select p.divisions from Professeur p where p.codeSynchro = :codeSynchro")
    List<Division> findDivisionsForProfesseur(@Param("codeSynchro") String codeSynchro);

    @Query("select d.niveau from Professeur p join p.divisions d where p.codeSynchro = :codeSynchro")
    List<Niveau> findNiveauxForProfesseur(@Param("codeSynchro") String codeSynchro);
}
