package altn72.tpfilrouge.dto;

import altn72.tpfilrouge.modele.Prestataire;

import java.util.Optional;

/**
 * Un prestataire et sa note moyenne, absente tant qu'il n'a pas été évalué (F10).
 */
public record PrestataireNoteDto(Prestataire prestataire, Optional<Double> noteMoyenne) {
}
