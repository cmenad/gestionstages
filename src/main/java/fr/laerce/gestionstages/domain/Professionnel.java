package fr.laerce.gestionstages.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 * Projet gestionstages
 * Pour LAERCE SAS
 * <p>
 * Créé le  06/02/2018.
 *
 * @author fred
 */

@Entity
public class Professionnel extends Individu{
    @Column
    private String fonction;

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
}
