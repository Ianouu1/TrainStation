package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.dto.EvaluationForm;
import altn72.tpfilrouge.service.EvaluationService;
import altn72.tpfilrouge.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Saisie d'une évaluation de prestataire (F10). Réservée au responsable
 * d'exploitation côté serveur (RG11).
 */
@Controller
public class EvaluationControleur {

    private final EvaluationService evaluationService;
    private final IncidentService incidentService;

    public EvaluationControleur(EvaluationService evaluationService, IncidentService incidentService) {
        this.evaluationService = evaluationService;
        this.incidentService = incidentService;
    }

    @GetMapping("/incidents/{id}/evaluation")
    public String afficherFormulaireEvaluation(@PathVariable("id") Integer idIncident, Model model) {
        model.addAttribute("incident", incidentService.obtenirIncident(idIncident));
        if (!model.containsAttribute("evaluationForm")) {
            model.addAttribute("evaluationForm", new EvaluationForm());
        }
        return "evaluations/nouvelle";
    }

    @PostMapping("/incidents/{id}/evaluation")
    public String enregistrerEvaluation(@PathVariable("id") Integer idIncident,
                                        @Valid @ModelAttribute EvaluationForm evaluationForm,
                                        BindingResult resultat,
                                        Model model) {
        if (resultat.hasErrors()) {
            model.addAttribute("incident", incidentService.obtenirIncident(idIncident));
            return "evaluations/nouvelle";
        }
        evaluationService.evaluer(idIncident, evaluationForm.getNote(), evaluationForm.getCommentaire());
        return "redirect:/incidents/" + idIncident;
    }
}
