package altn72.tpfilrouge.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Station du réseau de transport dans laquelle sont installés des équipements.
 */
@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_station")
    private Integer idStation;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    protected Station() {
        // Requis par JPA.
    }

    public Station(String nom) {
        this.nom = nom;
    }

    public Integer getIdStation() {
        return idStation;
    }

    public void setIdStation(Integer idStation) {
        this.idStation = idStation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Station{idStation=" + idStation + ", nom='" + nom + "'}";
    }
}
