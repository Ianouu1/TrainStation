package altn72.tpfilrouge.modele;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    /** Historique par statut, trié par date de résolution décroissante (F9, RG6, RG7). Requête dérivée. */
    List<Incident> findByStatutOrderByDateResolutionDesc(StatutIncident statut);

    /** Incidents actifs (non résolus) du tableau de bord (F11, RG6). Requête JPQL explicite. */
    @Query("select i from Incident i "
            + "where i.statut <> altn72.tpfilrouge.modele.StatutIncident.RESOLU "
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
