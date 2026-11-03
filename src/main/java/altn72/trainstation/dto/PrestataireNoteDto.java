package altn72.trainstation.dto;

import altn72.trainstation.modele.Prestataire;

import java.util.Optional;

/**
 * Un prestataire et sa note moyenne, absente tant qu'il n'a pas été évalué (F10).
 */
public record PrestataireNoteDto(Prestataire prestataire, Optional<Double> noteMoyenne) {
}
