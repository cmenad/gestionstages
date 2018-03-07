package fr.laerce.gestionstages.dao;

import fr.laerce.gestionstages.domain.Eleve;
import fr.laerce.gestionstages.domain.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EleveRepository extends JpaRepository<Eleve, Long> {

}
