package altn72.trainstation.controleur;

import altn72.trainstation.dto.DescriptionIncidentForm;
import altn72.trainstation.modele.Equipement;
import altn72.trainstation.service.EquipementService;
import altn72.trainstation.service.EvaluateurFragilite;
import altn72.trainstation.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Fiche d'un équipement (F3) et déclaration d'incident sur cet équipement (F4).
 */
@Controller
public class EquipementControleur {

    private static final String MESSAGE_INCIDENT_DECLARE = "Incident déclaré.";
    private static final String MESSAGE_INCIDENT_DECLARE_EQUIPEMENT_FRAGILE =
            "Incident déclaré. Attention : cet équipement est désormais fragile "
                    + "(trop d'incidents ouverts ces derniers jours).";

    private final EquipementService equipementService;
    private final IncidentService incidentService;
    private final EvaluateurFragilite evaluateurFragilite;

    public EquipementControleur(EquipementService equipementService,
                                IncidentService incidentService,
                                EvaluateurFragilite evaluateurFragilite) {
        this.equipementService = equipementService;
        this.incidentService = incidentService;
        this.evaluateurFragilite = evaluateurFragilite;
    }

    /** Fiche équipement avec la liste de ses incidents (F3) et le badge « Équipement fragile ». */
    @GetMapping("/equipements/{id}")
    public String afficherEquipement(@PathVariable("id") Integer idEquipement, Model model) {
        model.addAttribute("equipement", equipementService.obtenirEquipement(idEquipement));
        model.addAttribute("incidents", equipementService.listerIncidentsDeEquipement(idEquipement));
        model.addAttribute("equipementFragile", evaluateurFragilite.estFragile(idEquipement));
        return "equipements/detail";
    }

    /** Formulaire de déclaration d'un incident (F4). */
    @GetMapping("/equipements/{id}/incidents/nouveau")
    public String afficherFormulaireDeclaration(@PathVariable("id") Integer idEquipement, Model model) {
        Equipement equipement = equipementService.obtenirEquipement(idEquipement);
        model.addAttribute("equipement", equipement);
        if (!model.containsAttribute("descriptionIncidentForm")) {
            model.addAttribute("descriptionIncidentForm", new DescriptionIncidentForm());
        }
        return "incidents/nouveau";
    }

    /**
     * Enregistre la déclaration d'un incident (F4). Le message de confirmation
     * signale l'équipement s'il est fragile après la déclaration.
     */
    @PostMapping("/equipements/{id}/incidents")
    public String declarerIncident(@PathVariable("id") Integer idEquipement,
                                   @Valid @ModelAttribute DescriptionIncidentForm descriptionIncidentForm,
                                   BindingResult resultat,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (resultat.hasErrors()) {
            model.addAttribute("equipement", equipementService.obtenirEquipement(idEquipement));
            return "incidents/nouveau";
        }
        incidentService.declarerIncident(idEquipement, descriptionIncidentForm.getDescription());
        String messageConfirmation = evaluateurFragilite.estFragile(idEquipement)
                ? MESSAGE_INCIDENT_DECLARE_EQUIPEMENT_FRAGILE
                : MESSAGE_INCIDENT_DECLARE;
        redirectAttributes.addFlashAttribute("messageConfirmation", messageConfirmation);
        return "redirect:/equipements/" + idEquipement;
    }
}
