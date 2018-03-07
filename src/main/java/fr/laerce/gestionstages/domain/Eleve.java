package fr.laerce.gestionstages.domain;

import org.w3c.dom.NamedNodeMap;

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
public class Eleve extends Individu{
    @Column
    private String legal1Nom;
    @Column
    private String legal1Prenom;
    @Column
    private String legal1Commune;
    @Column
    private String legal1CodePostal;

    public String getLegal1Nom() {
        return legal1Nom;
    }

    public void setLegal1Nom(String legal1Nom) {
        this.legal1Nom = legal1Nom;
    }

    public String getLegal1Prenom() {
        return legal1Prenom;
    }

    public void setLegal1Prenom(String legal1Prenom) {
        this.legal1Prenom = legal1Prenom;
    }

    public String getLegal1Commune() {
        return legal1Commune;
    }

    public void setLegal1Commune(String legal1Commune) {
        this.legal1Commune = legal1Commune;
    }

    public String getLegal1CodePostal() {
        return legal1CodePostal;
    }

    public void setLegal1CodePostal(String legal1CodePostal) {
        this.legal1CodePostal = legal1CodePostal;
    }
}
