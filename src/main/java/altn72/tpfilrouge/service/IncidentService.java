package altn72.tpfilrouge.service;

import altn72.tpfilrouge.exception.RessourceIntrouvableException;
import altn72.tpfilrouge.exception.TransitionIncidentInterditeException;
import altn72.tpfilrouge.modele.Equipement;
import altn72.tpfilrouge.modele.EquipementRepository;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.IncidentRepository;
import altn72.tpfilrouge.modele.Prestataire;
import altn72.tpfilrouge.modele.PrestataireRepository;
import altn72.tpfilrouge.modele.StatutIncident;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Cœur métier du cycle de vie des incidents (F4 à F9).
 *
 * <p>Les règles de gestion (RG1, RG2, RG5, RG6, RG7) sont appliquées ici, jamais
 * dans les contrôleurs ni dans les vues. L'heure courante provient d'une horloge
 * injectée afin de rester testable.</p>
 */
@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final EquipementRepository equipementRepository;
    private final PrestataireRepository prestataireRepository;
    private final Clock horloge;

    public IncidentService(IncidentRepository incidentRepository,
                           EquipementRepository equipementRepository,
                           PrestataireRepository prestataireRepository,
                           Clock horloge) {
        this.incidentRepository = incidentRepository;
        this.equipementRepository = equipementRepository;
        this.prestataireRepository = prestataireRepository;
        this.horloge = horloge;
    }

    public Incident obtenirIncident(Integer idIncident) {
        return incidentRepository.findById(idIncident)
                .orElseThrow(() -> new RessourceIntrouvableException("Incident", idIncident));
    }

    /** Incidents actifs (non résolus) du tableau de bord (F11). */
    public List<Incident> listerIncidentsActifs() {
        return incidentRepository.trouverIncidentsActifs();
    }

    /** Historique des incidents résolus, du plus récent au plus ancien (F9, RG6, RG7). */
    public List<Incident> listerHistoriqueResolus() {
        return incidentRepository.findByStatutOrderByDateResolutionDesc(StatutIncident.RESOLU);
    }

    /**
     * Déclare un incident sur un équipement (F4). À l'ouverture, l'incident est
     * horodaté, sans prestataire et au statut NON_AFFECTE (RG1).
     */
    @Transactional
    public Incident declarerIncident(Integer idEquipement, String description) {
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new RessourceIntrouvableException("Équipement", idEquipement));
        Incident incident = new Incident(equipement, description, LocalDateTime.now(horloge));
        return incidentRepository.save(incident);
    }

    /** Modifie la description d'un incident (F6). */
    @Transactional
    public Incident modifierDescription(Integer idIncident, String description) {
        Incident incident = obtenirIncident(idIncident);
        incident.setDescription(description);
        return incidentRepository.save(incident);
    }

    /**
     * Affecte ou réaffecte un prestataire à un incident non résolu (F7, RG2).
     * Un incident résolu ne peut plus être affecté ni réaffecté (RG5).
     */
    @Transactional
    public Incident affecterPrestataire(Integer idIncident, Integer idPrestataire) {
        Incident incident = obtenirIncident(idIncident);
        if (incident.getStatut() == StatutIncident.RESOLU) {
            throw new TransitionIncidentInterditeException(
                    "Un incident résolu ne peut plus être affecté ni réaffecté à un prestataire.");
        }
        Prestataire prestataire = prestataireRepository.findById(idPrestataire)
                .orElseThrow(() -> new RessourceIntrouvableException("Prestataire", idPrestataire));
        incident.setPrestataire(prestataire);
        incident.setStatut(StatutIncident.AFFECTE);
        return incidentRepository.save(incident);
    }

    /**
     * Déclare un incident comme résolu (F8). Enregistre la date et l'heure de
     * résolution, postérieure ou égale à l'ouverture (RG5). Un incident déjà
     * résolu ne peut pas l'être une seconde fois.
     */
    @Transactional
    public Incident resoudreIncident(Integer idIncident) {
        Incident incident = obtenirIncident(idIncident);
        if (incident.getStatut() == StatutIncident.RESOLU) {
            throw new TransitionIncidentInterditeException("Cet incident est déjà résolu.");
        }
        incident.setDateResolution(LocalDateTime.now(horloge));
        incident.setStatut(StatutIncident.RESOLU);
        return incidentRepository.save(incident);
    }
}
