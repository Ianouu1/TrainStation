package altn72.trainstation.modele;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux prestataires.
 */
public interface PrestataireRepository extends JpaRepository<Prestataire, Integer> {

    /** Prestataires triés par nom (requête dérivée). */
    List<Prestataire> findAllByOrderByNomAsc();
}
