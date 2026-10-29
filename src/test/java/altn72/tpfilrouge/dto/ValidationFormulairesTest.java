package altn72.tpfilrouge.dto;

import altn72.tpfilrouge.modele.Evaluation;
import altn72.tpfilrouge.modele.Incident;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Vérifie que les données saisies sont validées dès l'entrée (Bean Validation),
 * avant d'atteindre les services ou la base.
 */
class ValidationFormulairesTest {

    private final Validator validateur = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void descriptionTropCourteEstRefusee() {
        DescriptionIncidentForm formulaire = new DescriptionIncidentForm("En panne");
        assertFalse(validateur.validate(formulaire).isEmpty());
    }

    @Test
    void descriptionPlusLongueQueLaColonneEstRefusee() {
        String description = "x".repeat(Incident.LONGUEUR_MAX_DESCRIPTION + 1);
        assertFalse(validateur.validate(new DescriptionIncidentForm(description)).isEmpty());
    }

    @Test
    void descriptionValideEstAcceptee() {
        DescriptionIncidentForm formulaire = new DescriptionIncidentForm("Cabine immobilisée entre deux niveaux");
        assertTrue(validateur.validate(formulaire).isEmpty());
    }

    @Test
    void noteEnDehorsDeUnACinqEstRefusee() {
        EvaluationForm formulaire = new EvaluationForm();
        formulaire.setNote(Evaluation.NOTE_MAXIMALE + 1);
        assertFalse(validateur.validate(formulaire).isEmpty());
    }
}
