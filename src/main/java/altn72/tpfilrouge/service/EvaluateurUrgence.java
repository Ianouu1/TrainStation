package altn72.tpfilrouge.service;

import altn72.tpfilrouge.modele.NiveauUrgence;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Calcule l'indicateur d'attention (RG3) d'un incident non affecté et non résolu,
 * à partir de la durée écoulée depuis son ouverture et de l'heure courante.
 *
 * <p>Les seuils sont définis ici, à un seul endroit, sous forme de constantes
 * nommées (pas de « nombre magique », §7.3). L'heure courante provient d'une
 * {@link Clock} injectée, ce qui rend le calcul testable avec une horloge figée.</p>
 */
@Service
public class EvaluateurUrgence {

    /** Seuil (en heures) à partir duquel l'incident passe de vert à orange, 48 h incluses (RG3). */
    public static final long SEUIL_ORANGE_EN_HEURES = 48L;

    /** Seuil (en heures) à partir duquel l'incident passe à rouge, 72 h incluses (RG3). */
    public static final long SEUIL_ROUGE_EN_HEURES = 72L;

    private final Clock horloge;

    public EvaluateurUrgence(Clock horloge) {
        this.horloge = horloge;
    }

    /**
     * Évalue le niveau d'urgence d'après la date d'ouverture de l'incident.
     *
     * @param dateOuverture date et heure d'ouverture de l'incident
     * @return vert en dessous de 48 h, orange de 48 h à moins de 72 h, rouge à partir de 72 h
     */
    public NiveauUrgence evaluer(LocalDateTime dateOuverture) {
        long dureeOuvertureEnHeures = calculerDureeOuvertureEnHeures(dateOuverture);
        if (dureeOuvertureEnHeures >= SEUIL_ROUGE_EN_HEURES) {
            return NiveauUrgence.ROUGE;
        }
        if (dureeOuvertureEnHeures >= SEUIL_ORANGE_EN_HEURES) {
            return NiveauUrgence.ORANGE;
        }
        return NiveauUrgence.VERT;
    }

    /** Nombre d'heures entières écoulées depuis l'ouverture, d'après l'heure courante. */
    public long calculerDureeOuvertureEnHeures(LocalDateTime dateOuverture) {
        return Duration.between(dateOuverture, LocalDateTime.now(horloge)).toHours();
    }
}
