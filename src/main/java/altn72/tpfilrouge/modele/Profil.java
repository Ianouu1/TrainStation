package altn72.tpfilrouge.modele;

/**
 * Profils d'utilisateurs de l'application (§3).
 * RESPONSABLE_EXPLOITATION dispose de la vision de supervision ; AGENT signale
 * les pannes sur le terrain. La répartition fine des droits est documentée dans
 * le journal (§5.6) et appliquée côté serveur (RG11).
 */
public enum Profil {
    RESPONSABLE_EXPLOITATION("Responsable d'exploitation"),
    AGENT("Agent");

    private final String libelle;

    Profil(String libelle) {
        this.libelle = libelle;
    }

    /** Libellé lisible affiché dans l'interface. */
    public String getLibelle() {
        return libelle;
    }
}
