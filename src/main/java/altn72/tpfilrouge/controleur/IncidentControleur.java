package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.dto.DescriptionIncidentForm;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.service.EvaluateurUrgence;
import altn72.tpfilrouge.service.EvaluationService;
import altn72.tpfilrouge.service.IncidentService;
import altn72.tpfilrouge.service.PrestataireService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Détail d'un incident (F5) et actions de son cycle de vie : modification (F6),
 * affectation d'un prestataire (F7), résolution (F8), et historique (F9).
 *
 * <p>Les actions sensibles sont déjà réservées au responsable d'exploitation côté
 * serveur (voir {@code ConfigurationSecurite}, RG11).</p>
 */
@Controller
public class IncidentControleur {

    private static final String VUE_DETAIL = "incidents/detail";

    private final IncidentService incidentService;
    private final PrestataireService prestataireService;
    private final EvaluationService evaluationService;
    private final EvaluateurUrgence evaluateurUrgence;

    public IncidentControleur(IncidentService incidentService,
                              PrestataireService prestataireService,
                              EvaluationService evaluationService,
                              EvaluateurUrgence evaluateurUrgence) {
        this.incidentService = incidentService;
        this.prestataireService = prestataireService;
        this.evaluationService = evaluationService;
        this.evaluateurUrgence = evaluateurUrgence;
    }

    /** Détail complet d'un incident (F5). */
    @GetMapping("/incidents/{id}")
    public String afficherIncident(@PathVariable("id") Integer idIncident, Model model) {
        Incident incident = preparerDetail(idIncident, model);
        model.addAttribute("descriptionIncidentForm", new DescriptionIncidentForm(incident.getDescription()));
        return VUE_DETAIL;
    }

    /** Modifie la description d'un incident (F6) ; une saisie invalide réaffiche le détail avec le message. */
    @PostMapping("/incidents/{id}/description")
    public String modifierDescription(@PathVariable("id") Integer idIncident,
                                      @Valid @ModelAttribute DescriptionIncidentForm descriptionIncidentForm,
                                      BindingResult resultat,
                                      Model model) {
        if (resultat.hasErrors()) {
            preparerDetail(idIncident, model);
            return VUE_DETAIL;
        }
        incidentService.modifierDescription(idIncident, descriptionIncidentForm.getDescription());
        return "redirect:/incidents/" + idIncident;
    }

    /** Affecte ou réaffecte un prestataire (F7). */
    @PostMapping("/incidents/{id}/affectation")
    public String affecterPrestataire(@PathVariable("id") Integer idIncident,
                                      @RequestParam Integer idPrestataire) {
        incidentService.affecterPrestataire(idIncident, idPrestataire);
        return "redirect:/incidents/" + idIncident;
    }

    /** Déclare un incident comme résolu (F8). */
    @PostMapping("/incidents/{id}/resolution")
    public String resoudreIncident(@PathVariable("id") Integer idIncident) {
        incidentService.resoudreIncident(idIncident);
        return "redirect:/incidents/" + idIncident;
    }

    /** Historique des incidents résolus, du plus récent au plus ancien (F9, RG7). */
    @GetMapping("/historique")
    public String afficherHistorique(Model model) {
        model.addAttribute("incidents", incidentService.listerHistoriqueResolus());
        return "incidents/historique";
    }

    private Incident preparerDetail(Integer idIncident, Model model) {
        Incident incident = incidentService.obtenirIncident(idIncident);
        model.addAttribute("incident", incident);
        model.addAttribute("dureeOuvertureEnHeures",
                evaluateurUrgence.calculerDureeOuvertureEnHeures(incident.getDateOuverture()));
        model.addAttribute("prestataires", prestataireService.listerPrestataires());
        evaluationService.trouverEvaluation(idIncident)
                .ifPresent(evaluation -> model.addAttribute("evaluation", evaluation));
        return incident;
    }
}
