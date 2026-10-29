package altn72.tpfilrouge.modele;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Accès aux équipements.
 */
public interface EquipementRepository extends JpaRepository<Equipement, Integer> {

    /** Équipements d'une station, triés par libellé (F2). */
    List<Equipement> findByStation_IdStationOrderByLibelleAsc(Integer idStation);

    /** Répartition des équipements par type (lot alpha, visualisation). Chaque ligne : [type, nombre]. */
    @Query("select e.type, count(e) from Equipement e group by e.type order by count(e) desc")
    List<Object[]> compterParType();
}
