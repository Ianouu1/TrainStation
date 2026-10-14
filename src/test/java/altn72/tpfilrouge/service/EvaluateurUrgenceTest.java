package altn72.tpfilrouge.service;

import altn72.tpfilrouge.modele.NiveauUrgence;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Vérifie les seuils de l'indicateur d'urgence (RG3) avec une horloge figée,
 * sans démarrer de contexte Spring.
 */
class EvaluateurUrgenceTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final LocalDateTime MAINTENANT = LocalDateTime.of(2026, 10, 14, 15, 0);

    private EvaluateurUrgence evaluateurAvecHorlogeFigee() {
        Clock horlogeFigee = Clock.fixed(MAINTENANT.atZone(ZONE).toInstant(), ZONE);
        return new EvaluateurUrgence(horlogeFigee);
    }

    @Test
    void incidentNonAffecteOuvertDepuis47HeuresEstVert() {
        NiveauUrgence niveau = evaluateurAvecHorlogeFigee().evaluer(MAINTENANT.minusHours(47));
        assertEquals(NiveauUrgence.VERT, niveau);
    }

    @Test
    void incidentNonAffecteOuvertDepuis48HeuresEstOrange() {
        NiveauUrgence niveau = evaluateurAvecHorlogeFigee().evaluer(MAINTENANT.minusHours(48));
        assertEquals(NiveauUrgence.ORANGE, niveau);
    }

    @Test
    void incidentNonAffecteOuvertDepuis71HeuresEstOrange() {
        NiveauUrgence niveau = evaluateurAvecHorlogeFigee().evaluer(MAINTENANT.minusHours(71));
        assertEquals(NiveauUrgence.ORANGE, niveau);
    }

    @Test
    void incidentNonAffecteOuvertDepuis72HeuresEstRouge() {
        NiveauUrgence niveau = evaluateurAvecHorlogeFigee().evaluer(MAINTENANT.minusHours(72));
        assertEquals(NiveauUrgence.ROUGE, niveau);
    }
}
