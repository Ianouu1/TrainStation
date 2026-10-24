package altn72.tpfilrouge.dto;

import altn72.tpfilrouge.modele.Evaluation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

/**
 * Formulaire de saisie d'une évaluation de prestataire (F10, RG8).
 */
public class EvaluationForm {

    @NotNull(message = "La note est obligatoire.")
    @Range(min = Evaluation.NOTE_MINIMALE, max = Evaluation.NOTE_MAXIMALE,
            message = "La note doit être comprise entre {min} et {max}.")
    private Integer note;

    @Size(max = Evaluation.LONGUEUR_MAX_COMMENTAIRE,
            message = "Le commentaire ne doit pas dépasser {max} caractères.")
    private String commentaire;

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
