package altn72.tpfilrouge.dto;

import altn72.tpfilrouge.modele.Incident;

import java.time.LocalDateTime;

/**
 * Représentation d'un incident exposée par l'API REST (F12).
 *
 * <p>Vue à plat (sans associations JPA) : elle fixe le contrat de l'API,
 * indépendamment du modèle de persistance, et évite de sérialiser des proxies
 * de chargement différé.</p>
 */
public record IncidentApiDto(
        Integer id,
        String description,
        String statut,
        LocalDateTime dateOuverture,
        LocalDateTime dateResolution,
        String equipementReference,
        String station,
        String prestataire) {

    public static IncidentApiDto creerDepuis(Incident incident) {
        return new IncidentApiDto(
                incident.getIdIncident(),
                incident.getDescription(),
                incident.getStatut().name(),
                incident.getDateOuverture(),
                incident.getDateResolution(),
                incident.getEquipement().getReference(),
                incident.getEquipement().getStation().getNom(),
                incident.getPrestataire() != null ? incident.getPrestataire().getNom() : null);
    }
}
