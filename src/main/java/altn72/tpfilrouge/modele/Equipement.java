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

/**
 * Équipement installé dans une station (ascenseur, escalier mécanique, portique...).
 * La référence est unique sur l'ensemble du parc (RG9).
 */
@Entity
@Table(name = "equipement")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipement")
    private Integer idEquipement;

    @Column(name = "reference", nullable = false, unique = true, length = 50)
    private String reference;

    @Column(name = "libelle", nullable = false, length = 150)
    private String libelle;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false, length = 20)
    private EtatEquipement etat;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_station", nullable = false)
    private Station station;

    protected Equipement() {
        // Requis par JPA.
    }

    public Equipement(String reference, String libelle, String type, EtatEquipement etat, Station station) {
        this.reference = reference;
        this.libelle = libelle;
        this.type = type;
        this.etat = etat;
        this.station = station;
    }

    public Integer getIdEquipement() {
        return idEquipement;
    }

    public void setIdEquipement(Integer idEquipement) {
        this.idEquipement = idEquipement;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EtatEquipement getEtat() {
        return etat;
    }

    public void setEtat(EtatEquipement etat) {
        this.etat = etat;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return "Equipement{idEquipement=" + idEquipement + ", reference='" + reference + "', type='" + type + "'}";
    }
}
