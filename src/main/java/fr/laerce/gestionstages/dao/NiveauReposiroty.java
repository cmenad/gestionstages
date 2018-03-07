package fr.laerce.gestionstages.dao;

import fr.laerce.gestionstages.domain.Niveau;
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
public interface NiveauReposiroty extends JpaRepository<Niveau, Long> {
    List<Niveau> findAllByOrderByIdAsc();
    public Niveau findByCode(String code);
}
