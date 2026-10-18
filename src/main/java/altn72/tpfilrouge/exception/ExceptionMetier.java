package altn72.tpfilrouge.exception;

/**
 * Classe de base de toutes les exceptions métier de l'application.
 *
 * <p>Non vérifiée (hérite de {@link RuntimeException}) : {@code @Transactional}
 * déclenche alors un rollback automatique. Chaque sous-classe porte un message
 * clair, en français, et conserve si besoin la cause d'origine.</p>
 */
public abstract class ExceptionMetier extends RuntimeException {

    protected ExceptionMetier(String message) {
        super(message);
    }

    protected ExceptionMetier(String message, Throwable cause) {
        super(message, cause);
    }
}
