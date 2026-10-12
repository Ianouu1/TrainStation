package altn72.tpfilrouge.modele;

/**
 * Indicateur d'attention d'un incident non affecté et non résolu (RG3).
 * Calculé à partir de l'heure courante, jamais persisté (RG4) : il n'a donc
 * volontairement aucune correspondance en base de données.
 */
public enum NiveauUrgence {
    VERT,
    ORANGE,
    ROUGE
}
