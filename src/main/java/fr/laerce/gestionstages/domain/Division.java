package fr.laerce.gestionstages.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Projet pfmp-base
 * Pour LAERCE SAS
 * <p>
 * Créé le  11/01/2018.
 *
 * @author fred
 */
@Entity
public class Division implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(unique = true)
    private String code;
    @Column
    private String libelle;
    @ManyToOne (cascade = CascadeType.ALL)
    private Niveau niveau;
    @ManyToMany(mappedBy = "divisions")
    private Set<Professeur> professeurs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Set<Professeur> getProfesseurs() {
        return professeurs;
    }

    public void setProfesseurs(Set<Professeur> divisions) {
        this.professeurs = divisions;
    }

    public void addProfesseur(Professeur professeur){
        if(this.professeurs == null){
            professeurs = new HashSet<>();
        }
        professeurs.add(professeur);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Division division = (Division) o;
        return Objects.equals(getId(), division.getId()) &&
                Objects.equals(getCode(), division.getCode()) &&
                Objects.equals(getLibelle(), division.getLibelle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getLibelle());
    }

    @Override
    public String toString() {
        return "Division{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                '}';
    }

}
