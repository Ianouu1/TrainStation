package altn72.tpfilrouge.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Prestataire susceptible de prendre en charge un incident.
 */
@Entity
@Table(name = "prestataire")
public class Prestataire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestataire")
    private Integer idPrestataire;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "contact", nullable = false, length = 150)
    private String contact;

    protected Prestataire() {
        // Requis par JPA.
    }

    public Prestataire(String nom, String contact) {
        this.nom = nom;
        this.contact = contact;
    }

    public Integer getIdPrestataire() {
        return idPrestataire;
    }

    public void setIdPrestataire(Integer idPrestataire) {
        this.idPrestataire = idPrestataire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Prestataire{idPrestataire=" + idPrestataire + ", nom='" + nom + "'}";
    }
}
