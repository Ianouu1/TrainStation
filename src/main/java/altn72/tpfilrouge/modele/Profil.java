package altn72.tpfilrouge.modele;

/**
 * Profils d'utilisateurs de l'application (§3).
 * RESPONSABLE_EXPLOITATION dispose de la vision de supervision ; AGENT signale
 * les pannes sur le terrain. La répartition fine des droits est documentée dans
 * le journal (§5.6) et appliquée côté serveur (RG11).
 */
public enum Profil {
    RESPONSABLE_EXPLOITATION,
    AGENT
}
