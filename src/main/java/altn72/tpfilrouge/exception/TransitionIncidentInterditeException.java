package altn72.tpfilrouge.exception;

/**
 * Levée lorsqu'une transition d'état d'un incident n'est pas autorisée
 * (par ex. affecter ou réaffecter un incident déjà résolu, RG5).
 * Traduite en HTTP 409 (conflit).
 */
public class TransitionIncidentInterditeException extends ExceptionMetier {

    public TransitionIncidentInterditeException(String message) {
        super(message);
    }
}
