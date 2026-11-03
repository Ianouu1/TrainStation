package altn72.trainstation.exception;

/**
 * Classe de base de toutes les exceptions métier de l'application.
 *
 * <p>Non vérifiée (hérite de {@link RuntimeException}) : {@code @Transactional}
 * déclenche alors un rollback automatique. Chaque sous-classe porte un message
 * clair, en français, et conserve si besoin la cause d'origine.</p>
 */
public abstract class MetierException extends RuntimeException {

    protected MetierException(String message) {
        super(message);
    }

    protected MetierException(String message, Throwable cause) {
        super(message, cause);
    }
}
