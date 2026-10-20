package altn72.tpfilrouge.dto;

/**
 * Indicateurs synthétiques du tableau de bord de supervision (F11).
 */
public record IndicateursSupervisionDto(
        long totalActifs,
        long nonAffectes,
        long affectes,
        long verts,
        long oranges,
        long rouges) {
}
