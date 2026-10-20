package altn72.tpfilrouge.service;

import altn72.tpfilrouge.dto.IndicateursSupervisionDto;
import altn72.tpfilrouge.dto.LigneSupervisionDto;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.NiveauUrgence;
import altn72.tpfilrouge.modele.StatutIncident;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Construit la vue du tableau de bord de supervision (F11).
 *
 * <p>Chaque incident actif est enrichi de son indicateur d'attention : vert,
 * orange ou rouge pour un incident non affecté (RG3), rien pour un incident
 * affecté, qui reçoit une présentation distincte (RG4). Les incidents résolus
 * n'y figurent pas (RG6).</p>
 */
@Service
public class SupervisionService {

    private final IncidentService incidentService;
    private final EvaluateurUrgence evaluateurUrgence;

    public SupervisionService(IncidentService incidentService, EvaluateurUrgence evaluateurUrgence) {
        this.incidentService = incidentService;
        this.evaluateurUrgence = evaluateurUrgence;
    }

    /** Lignes du tableau de bord : incidents actifs enrichis de leur indicateur. */
    public List<LigneSupervisionDto> construireTableauDeBord() {
        return incidentService.listerIncidentsActifs().stream()
                .map(this::superviser)
                .toList();
    }

    /** Indicateurs synthétiques calculés à partir des lignes du tableau de bord. */
    public IndicateursSupervisionDto calculerIndicateurs(List<LigneSupervisionDto> lignes) {
        long affectes = lignes.stream().filter(LigneSupervisionDto::estAffecte).count();
        long verts = compter(lignes, NiveauUrgence.VERT);
        long oranges = compter(lignes, NiveauUrgence.ORANGE);
        long rouges = compter(lignes, NiveauUrgence.ROUGE);
        return new IndicateursSupervisionDto(
                lignes.size(), lignes.size() - affectes, affectes, verts, oranges, rouges);
    }

    private LigneSupervisionDto superviser(Incident incident) {
        if (incident.getStatut() == StatutIncident.AFFECTE) {
            return new LigneSupervisionDto(incident, null);
        }
        return new LigneSupervisionDto(incident, evaluateurUrgence.evaluer(incident.getDateOuverture()));
    }

    private long compter(List<LigneSupervisionDto> lignes, NiveauUrgence niveau) {
        return lignes.stream().filter(ligne -> niveau == ligne.niveauUrgence()).count();
    }
}
