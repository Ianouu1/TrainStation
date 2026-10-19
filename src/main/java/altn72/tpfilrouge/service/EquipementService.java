package altn72.tpfilrouge.service;

import altn72.tpfilrouge.exception.RessourceIntrouvableException;
import altn72.tpfilrouge.modele.Equipement;
import altn72.tpfilrouge.modele.EquipementRepository;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cas d'usage liés aux équipements (F2, F3).
 */
@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;
    private final IncidentRepository incidentRepository;

    public EquipementService(EquipementRepository equipementRepository, IncidentRepository incidentRepository) {
        this.equipementRepository = equipementRepository;
        this.incidentRepository = incidentRepository;
    }

    /** Équipements d'une station (F2). */
    public List<Equipement> listerEquipementsParStation(Integer idStation) {
        return equipementRepository.findByStation_IdStationOrderByLibelleAsc(idStation);
    }

    public Equipement obtenirEquipement(Integer idEquipement) {
        return equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new RessourceIntrouvableException("Équipement", idEquipement));
    }

    /** Incidents d'un équipement, du plus récent au plus ancien (F3, RG7). */
    public List<Incident> listerIncidentsDeEquipement(Integer idEquipement) {
        return incidentRepository.findByEquipement_IdEquipementOrderByDateOuvertureDesc(idEquipement);
    }
}
