package altn72.tpfilrouge.dto;

import altn72.tpfilrouge.modele.Incident;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Formulaire de déclaration (F4) et de modification de la description (F6) d'un incident.
 */
public class DescriptionIncidentForm {

    @NotBlank(message = "La description de l'incident est obligatoire.")
    @Size(min = Incident.LONGUEUR_MIN_DESCRIPTION, max = Incident.LONGUEUR_MAX_DESCRIPTION,
            message = "La description doit contenir entre {min} et {max} caractères.")
    private String description;

    public DescriptionIncidentForm() {
        // Requis par Spring MVC pour lier les champs du formulaire.
    }

    public DescriptionIncidentForm(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
