package fr.laerce.gestionstages.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * Projet pfmp-base
 * Pour LAERCE SAS
 * <p>
 * Créé le  11/01/2018.
 *
 * @author fred
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Individu implements Cloneable {

    @Column(nullable = false)
    String civilite;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column
    private String nom;
    @Column
    private String prenom;
    @Column
    private String codeSynchro;
    @Column
    private String telephoneMobile;
    @Column
    private String telephoneFixe;
    @Column
    private String email;

    @Column(unique = true)
    private String login;

    @Column
    private String mdp;

    @Column
    private String mdpOrigine;

    /**
     * Les rôles de l'utiilisateur éventuellement séparés par une
     * virgule
     */
    @Column
    private String roles;

    public Individu() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public String getCodeSynchro() {
        return codeSynchro;
    }

    public void setCodeSynchro(String codeSynchro) {
        this.codeSynchro = codeSynchro;
    }

    public String getTelephoneMobile() {
        return telephoneMobile;
    }

    public void setTelephoneMobile(String telephoneMobile) {
        this.telephoneMobile = telephoneMobile;
    }

    public String getTelephoneFixe() {
        return telephoneFixe;
    }

    public void setTelephoneFixe(String telephoneFixe) {
        this.telephoneFixe = telephoneFixe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getMdpOrigine() {
        return mdpOrigine;
    }

    public void setMdpOrigine(String mdpOrigine) {
        this.mdpOrigine = mdpOrigine;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isAdmin(){
        return getRoles().contains("ADMIN");
    }

    public boolean isUser(){
        return getRoles().contains("USER");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individu individu = (Individu) o;
        return Objects.equals(codeSynchro, individu.codeSynchro);
    }

    @Override
    public int hashCode() {

        return Objects.hash(codeSynchro);
    }

    @Override
    public String toString() {
        return "Individu{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +

                ", codeSynchro='" + codeSynchro + '\'' +
                ", telephoneMobile='" + telephoneMobile + '\'' +
                ", telephoneFixe='" + telephoneFixe + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
