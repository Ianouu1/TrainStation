package altn72.tpfilrouge.service;

import altn72.tpfilrouge.exception.EvaluationInvalideException;
import altn72.tpfilrouge.modele.Evaluation;
import altn72.tpfilrouge.modele.EvaluationRepository;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.StatutIncident;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Saisie des évaluations de prestataires (F10, RG8).
 */
@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final IncidentService incidentService;
    private final Clock horloge;

    public EvaluationService(EvaluationRepository evaluationRepository,
                             IncidentService incidentService,
                             Clock horloge) {
        this.evaluationRepository = evaluationRepository;
        this.incidentService = incidentService;
        this.horloge = horloge;
    }

    /**
     * Enregistre l'évaluation de l'intervention d'un prestataire (RG8) :
     * l'incident doit être résolu et avoir été affecté à un prestataire, ne pas
     * être déjà évalué, et la note doit être un entier de 1 à 5.
     */
    @Transactional
    public Evaluation evaluer(Integer idIncident, int note, String commentaire) {
        Incident incident = incidentService.obtenirIncident(idIncident);

        if (incident.getStatut() != StatutIncident.RESOLU) {
            throw new EvaluationInvalideException("Seul un incident résolu peut être évalué.");
        }
        if (incident.getPrestataire() == null) {
            throw new EvaluationInvalideException(
                    "Seul un incident affecté à un prestataire peut être évalué.");
        }
        if (note < Evaluation.NOTE_MINIMALE || note > Evaluation.NOTE_MAXIMALE) {
            throw new EvaluationInvalideException("La note doit être un entier compris entre "
                    + Evaluation.NOTE_MINIMALE + " et " + Evaluation.NOTE_MAXIMALE + ".");
        }
        if (evaluationRepository.existsByIncident_IdIncident(idIncident)) {
            throw new EvaluationInvalideException("Cet incident a déjà été évalué.");
        }

        Evaluation evaluation = new Evaluation(incident, note, commentaire, LocalDateTime.now(horloge));
        return evaluationRepository.save(evaluation);
    }

    /** Évaluation d'un incident, si elle existe (RG8 : une seule par incident). */
    public Optional<Evaluation> trouverEvaluation(Integer idIncident) {
        return evaluationRepository.findByIncident_IdIncident(idIncident);
    }
}
