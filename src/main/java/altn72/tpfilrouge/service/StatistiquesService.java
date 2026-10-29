package altn72.tpfilrouge.service;

import altn72.tpfilrouge.dto.PointGraphiqueDto;
import altn72.tpfilrouge.modele.EquipementRepository;
import altn72.tpfilrouge.modele.EvaluationRepository;
import altn72.tpfilrouge.modele.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Agrégations pour la page de visualisation (lot alpha).
 *
 * <p>Transforme les résultats des requêtes d'agrégation (JPQL et SQL natif) en
 * points de données prêts à être tracés.</p>
 */
@Service
public class StatistiquesService {

    private final IncidentRepository incidentRepository;
    private final EquipementRepository equipementRepository;
    private final EvaluationRepository evaluationRepository;

    public StatistiquesService(IncidentRepository incidentRepository,
                               EquipementRepository equipementRepository,
                               EvaluationRepository evaluationRepository) {
        this.incidentRepository = incidentRepository;
        this.equipementRepository = equipementRepository;
        this.evaluationRepository = evaluationRepository;
    }

    /** Nombre d'incidents actifs par station (requête SQL native). */
    public List<PointGraphiqueDto> compterIncidentsActifsParStation() {
        return convertir(incidentRepository.compterIncidentsActifsParStation());
    }

    /** Répartition des équipements par type (JPQL). */
    public List<PointGraphiqueDto> compterEquipementsParType() {
        return convertir(equipementRepository.compterParType());
    }

    /** Note moyenne par prestataire (JPQL). */
    public List<PointGraphiqueDto> calculerNoteMoyenneParPrestataire() {
        return convertir(evaluationRepository.calculerNoteMoyenneParPrestataire());
    }

    private List<PointGraphiqueDto> convertir(List<Object[]> lignes) {
        return lignes.stream()
                .map(ligne -> new PointGraphiqueDto(
                        String.valueOf(ligne[0]),
                        ((Number) ligne[1]).doubleValue()))
                .toList();
    }
}
