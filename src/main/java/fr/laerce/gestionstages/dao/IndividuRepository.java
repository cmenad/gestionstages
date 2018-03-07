package fr.laerce.gestionstages.dao;

import fr.laerce.gestionstages.domain.Division;
import fr.laerce.gestionstages.domain.Individu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Projet gestionstages
 * Pour LAERCE SAS
 * <p>
 * Créé le  05/02/2018.
 *
 * @author fred
 */
public interface IndividuRepository extends JpaRepository<Individu, Long>{
    List<Individu> findAllByOrderByIdAsc();
    public Individu findByCodeSynchro(String code);
    public int countAllByLogin(String login);
    public Individu findByLogin(String login);
}
