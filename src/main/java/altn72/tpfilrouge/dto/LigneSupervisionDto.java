package altn72.tpfilrouge.dto;

import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.NiveauUrgence;

/**
 * Ligne du tableau de bord de supervision (F11).
 *
 * <p>Pour un incident non affecté, {@code niveauUrgence} porte l'indicateur
 * vert/orange/rouge (RG3). Pour un incident affecté, il vaut {@code null} :
 * l'incident reçoit une présentation distincte et ne porte aucun de ces
 * indicateurs (RG4).</p>
 */
public record LigneSupervisionDto(Incident incident, NiveauUrgence niveauUrgence) {

    public boolean estAffecte() {
        return niveauUrgence == null;
    }
}
