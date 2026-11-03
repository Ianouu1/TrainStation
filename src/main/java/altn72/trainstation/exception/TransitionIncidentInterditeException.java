package altn72.trainstation.exception;

/**
 * Levée lorsqu'une transition d'état d'un incident n'est pas autorisée
 * (par ex. affecter ou réaffecter un incident déjà résolu, RG5).
 * Traduite en HTTP 409 (conflit).
 */
public class TransitionIncidentInterditeException extends MetierException {

    public TransitionIncidentInterditeException(String message) {
        super(message);
    }
}
