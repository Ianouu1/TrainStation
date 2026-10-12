package altn72.tpfilrouge.modele;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux équipements.
 */
public interface EquipementRepository extends JpaRepository<Equipement, Integer> {

    /** Équipements d'une station, triés par libellé (F2). */
    List<Equipement> findByStation_IdStationOrderByLibelleAsc(Integer idStation);
}
