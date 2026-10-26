package altn72.tpfilrouge.service;

import altn72.tpfilrouge.dto.IncidentApiDto;
import altn72.tpfilrouge.modele.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lecture et mappage des incidents pour l'API REST (F12).
 *
 * <p>Le mappage vers {@link IncidentApiDto} se fait dans la transaction
 * ({@code @Transactional}) afin de charger les associations nécessaires
 * (équipement, station, prestataire) alors que la session est encore ouverte.</p>
 */
@Service
public class ApiIncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentService incidentService;

    public ApiIncidentService(IncidentRepository incidentRepository, IncidentService incidentService) {
        this.incidentRepository = incidentRepository;
        this.incidentService = incidentService;
    }

    @Transactional(readOnly = true)
    public List<IncidentApiDto> lister() {
        return incidentRepository.findAll().stream().map(IncidentApiDto::creerDepuis).toList();
    }

    @Transactional(readOnly = true)
    public IncidentApiDto obtenir(Integer idIncident) {
        return IncidentApiDto.creerDepuis(incidentService.obtenirIncident(idIncident));
    }

    @Transactional
    public IncidentApiDto resoudre(Integer idIncident) {
        return IncidentApiDto.creerDepuis(incidentService.resoudreIncident(idIncident));
    }

    @Transactional
    public IncidentApiDto affecter(Integer idIncident, Integer idPrestataire) {
        return IncidentApiDto.creerDepuis(incidentService.affecterPrestataire(idIncident, idPrestataire));
    }
}
