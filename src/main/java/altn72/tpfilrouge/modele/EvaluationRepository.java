package altn72.tpfilrouge.modele;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Accès aux évaluations.
 */
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    /** Évaluation d'un incident, s'il en a une (RG8). Requête dérivée. */
    Optional<Evaluation> findByIncident_IdIncident(Integer idIncident);

    /** Vérifie qu'un incident n'est pas déjà évalué avant d'en saisir une (RG8). */
    boolean existsByIncident_IdIncident(Integer idIncident);

    /** Note moyenne d'un prestataire (F10). Requête JPQL d'agrégation. */
    @Query("select avg(ev.note) from Evaluation ev "
            + "where ev.incident.prestataire.idPrestataire = :idPrestataire")
    Double calculerNoteMoyennePrestataire(@Param("idPrestataire") Integer idPrestataire);
}
