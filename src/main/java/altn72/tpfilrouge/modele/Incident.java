package altn72.tpfilrouge.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Incident déclaré sur un équipement.
 *
 * <p>À l'ouverture, un incident est rattaché à un équipement, horodaté et sans
 * prestataire (RG1). Il est ensuite affecté puis résolu (RG2). Une fois résolu,
 * il porte une date de résolution postérieure ou égale à l'ouverture (RG5).</p>
 */
@Entity
@Table(name = "incident")
public class Incident {

    /** Longueur minimale d'une description, pour qu'elle soit exploitable par le prestataire. */
    public static final int LONGUEUR_MIN_DESCRIPTION = 10;

    /** Longueur maximale d'une description, égale à la taille de la colonne. */
    public static final int LONGUEUR_MAX_DESCRIPTION = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident")
    private Integer idIncident;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipement", nullable = false)
    private Equipement equipement;

    @Column(name = "description", nullable = false, length = LONGUEUR_MAX_DESCRIPTION)
    private String description;

    @Column(name = "date_ouverture", nullable = false)
    private LocalDateTime dateOuverture;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutIncident statut;

    /** Null tant que l'incident n'est pas affecté (RG1, RG2). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestataire")
    private Prestataire prestataire;

    /** Null tant que l'incident n'est pas résolu (RG5). */
    @Column(name = "date_resolution")
    private LocalDateTime dateResolution;

    protected Incident() {
        // Requis par JPA.
    }

    public Incident(Equipement equipement, String description, LocalDateTime dateOuverture) {
        this.equipement = equipement;
        this.description = description;
        this.dateOuverture = dateOuverture;
        this.statut = StatutIncident.NON_AFFECTE;
    }

    public Integer getIdIncident() {
        return idIncident;
    }

    public void setIdIncident(Integer idIncident) {
        this.idIncident = idIncident;
    }

    public Equipement getEquipement() {
        return equipement;
    }

    public void setEquipement(Equipement equipement) {
        this.equipement = equipement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDateTime dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public StatutIncident getStatut() {
        return statut;
    }

    public void setStatut(StatutIncident statut) {
        this.statut = statut;
    }

    public Prestataire getPrestataire() {
        return prestataire;
    }

    public void setPrestataire(Prestataire prestataire) {
        this.prestataire = prestataire;
    }

    public LocalDateTime getDateResolution() {
        return dateResolution;
    }

    public void setDateResolution(LocalDateTime dateResolution) {
        this.dateResolution = dateResolution;
    }

    @Override
    public String toString() {
        return "Incident{idIncident=" + idIncident + ", statut=" + statut + ", dateOuverture=" + dateOuverture + "}";
    }
}
