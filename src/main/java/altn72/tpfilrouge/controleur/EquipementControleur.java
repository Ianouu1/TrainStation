package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.dto.DescriptionIncidentForm;
import altn72.tpfilrouge.modele.Equipement;
import altn72.tpfilrouge.service.EquipementService;
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
 * Fiche d'un équipement (F3) et déclaration d'incident sur cet équipement (F4).
 */
@Controller
public class EquipementControleur {

    private final EquipementService equipementService;
    private final IncidentService incidentService;

    public EquipementControleur(EquipementService equipementService, IncidentService incidentService) {
        this.equipementService = equipementService;
        this.incidentService = incidentService;
    }

    /** Fiche équipement avec la liste de ses incidents (F3). */
    @GetMapping("/equipements/{id}")
    public String afficherEquipement(@PathVariable("id") Integer idEquipement, Model model) {
        model.addAttribute("equipement", equipementService.obtenirEquipement(idEquipement));
        model.addAttribute("incidents", equipementService.listerIncidentsDeEquipement(idEquipement));
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

    /** Enregistre la déclaration d'un incident (F4). */
    @PostMapping("/equipements/{id}/incidents")
    public String declarerIncident(@PathVariable("id") Integer idEquipement,
                                   @Valid @ModelAttribute DescriptionIncidentForm descriptionIncidentForm,
                                   BindingResult resultat,
                                   Model model) {
        if (resultat.hasErrors()) {
            model.addAttribute("equipement", equipementService.obtenirEquipement(idEquipement));
            return "incidents/nouveau";
        }
        incidentService.declarerIncident(idEquipement, descriptionIncidentForm.getDescription());
        return "redirect:/equipements/" + idEquipement;
    }
}
