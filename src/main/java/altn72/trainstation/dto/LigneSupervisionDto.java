package altn72.trainstation.dto;

import altn72.trainstation.modele.Incident;
import altn72.trainstation.modele.NiveauUrgence;

/**
 * Ligne du tableau de bord de supervision (F11).
 *
 * <p>Pour un incident non affecté, {@code niveauUrgence} porte l'indicateur
 * vert/orange/rouge (RG3). Pour un incident affecté, il vaut {@code null} :
 * l'incident reçoit une présentation distincte et ne porte aucun de ces
 * indicateurs (RG4).</p>
 *
 * <p>{@code equipementFragile} indique si l'équipement de l'incident est fragile
 * à l'instant de la consultation (évolution « Équipement fragile », EF3).</p>
 */
public record LigneSupervisionDto(Incident incident, NiveauUrgence niveauUrgence, boolean equipementFragile) {

    public boolean estAffecte() {
        return niveauUrgence == null;
    }
}
