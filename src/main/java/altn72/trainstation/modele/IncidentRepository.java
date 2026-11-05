package altn72.trainstation.modele;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Accès aux incidents.
 *
 * <p>Ce repository combine des requêtes dérivées du nom des méthodes, une requête
 * explicite en JPQL et, à titre d'illustration, une unique requête SQL native dans
 * tout le projet (analysée dans le journal, section CHOIX TECHNIQUES).</p>
 */
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    /** Incidents d'un équipement, du plus récent au plus ancien (F3, RG7). Requête dérivée. */
    List<Incident> findByEquipement_IdEquipementOrderByDateOuvertureDesc(Integer idEquipement);

    /**
     * Nombre d'incidents d'un équipement ouverts depuis une date donnée, incluse,
     * quel que soit leur statut (EF1, EF2). Requête dérivée.
     */
    long countByEquipement_IdEquipementAndDateOuvertureGreaterThanEqual(Integer idEquipement,
                                                                         LocalDateTime debutFenetre);

    /**
     * Identifiants des équipements ayant au moins {@code seuil} incidents ouverts
     * depuis {@code debutFenetre}, incluse, tous statuts confondus (EF1 à EF3).
     * Une seule requête pour tout le tableau de bord, au lieu d'un comptage par ligne.
     */
    @Query("select i.equipement.idEquipement from Incident i "
            + "where i.dateOuverture >= :debutFenetre "
            + "group by i.equipement.idEquipement "
            + "having count(i) >= :seuil")
    List<Integer> trouverEquipementsAvecAuMoinsNIncidentsDepuis(@Param("debutFenetre") LocalDateTime debutFenetre,
                                                                @Param("seuil") long seuil);

    /** Historique par statut, trié par date de résolution décroissante (F9, RG6, RG7). Requête dérivée. */
    List<Incident> findByStatutOrderByDateResolutionDesc(StatutIncident statut);

    /** Incidents actifs (non résolus) du tableau de bord (F11, RG6). Requête JPQL explicite. */
    @Query("select i from Incident i "
            + "where i.statut <> altn72.trainstation.modele.StatutIncident.RESOLU "
            + "order by i.dateOuverture asc")
    List<Incident> trouverIncidentsActifs();

    /**
     * Nombre d'incidents actifs par station (indicateur de supervision).
     * Unique requête SQL native du projet : analyse critique dans le journal.
     * Chaque ligne retournée est [nomStation, nombreIncidentsActifs].
     */
    @Query(value = "SELECT s.nom AS station, COUNT(i.id_incident) AS nb_actifs "
            + "FROM incident i "
            + "JOIN equipement e ON e.id_equipement = i.id_equipement "
            + "JOIN station s ON s.id_station = e.id_station "
            + "WHERE i.statut <> 'RESOLU' "
            + "GROUP BY s.id_station, s.nom "
            + "ORDER BY nb_actifs DESC",
            nativeQuery = true)
    List<Object[]> compterIncidentsActifsParStation();
}
