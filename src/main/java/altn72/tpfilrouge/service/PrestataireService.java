package altn72.tpfilrouge.service;

import altn72.tpfilrouge.dto.PrestataireNoteDto;
import altn72.tpfilrouge.modele.EvaluationRepository;
import altn72.tpfilrouge.modele.Prestataire;
import altn72.tpfilrouge.modele.PrestataireRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Cas d'usage liés aux prestataires, dont leur note moyenne (F10).
 */
@Service
public class PrestataireService {

    private final PrestataireRepository prestataireRepository;
    private final EvaluationRepository evaluationRepository;

    public PrestataireService(PrestataireRepository prestataireRepository,
                              EvaluationRepository evaluationRepository) {
        this.prestataireRepository = prestataireRepository;
        this.evaluationRepository = evaluationRepository;
    }

    public List<Prestataire> listerPrestataires() {
        return prestataireRepository.findAllByOrderByNomAsc();
    }

    /** Prestataires triés par nom, chacun avec sa note moyenne s'il a été évalué (F10). */
    public List<PrestataireNoteDto> listerPrestatairesAvecNoteMoyenne() {
        return listerPrestataires().stream()
                .map(prestataire -> new PrestataireNoteDto(prestataire,
                        calculerNoteMoyenne(prestataire.getIdPrestataire())))
                .toList();
    }

    private Optional<Double> calculerNoteMoyenne(Integer idPrestataire) {
        return Optional.ofNullable(evaluationRepository.calculerNoteMoyennePrestataire(idPrestataire));
    }
}
