package altn72.tpfilrouge.dto;

/**
 * Critères de filtrage et de tri du tableau de bord (lot alpha, supervision avancée).
 *
 * @param idStation filtrer sur une station (null = toutes)
 * @param niveau    filtrer sur un indicateur : VERT, ORANGE, ROUGE ou AFFECTE (null/vide = tous)
 * @param tri       ordre d'affichage : URGENCE (défaut) ou ANCIENNETE
 */
public record CritereSupervisionForm(Integer idStation, String niveau, String tri) {

    public boolean aUnFiltreDeNiveau() {
        return niveau != null && !niveau.isBlank();
    }

    public boolean estTrieParAnciennete() {
        return "ANCIENNETE".equalsIgnoreCase(tri);
    }
}
