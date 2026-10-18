package altn72.tpfilrouge.exception;

/**
 * Levée lorsqu'une évaluation ne respecte pas les règles RG8 :
 * incident non résolu ou non affecté, incident déjà évalué, ou note hors de [1, 5].
 * Traduite en HTTP 409 (conflit) ou 400 (donnée invalide) selon le cas.
 */
public class EvaluationInvalideException extends ExceptionMetier {

    public EvaluationInvalideException(String message) {
        super(message);
    }
}
