package altn72.tpfilrouge.modele;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux stations.
 */
public interface StationRepository extends JpaRepository<Station, Integer> {

    /** Stations triées par nom (requête dérivée du nom de la méthode). */
    List<Station> findAllByOrderByNomAsc();
}
