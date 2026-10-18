package altn72.tpfilrouge.exception;

/**
 * Levée lorsqu'une ressource demandée par son identifiant n'existe pas.
 * Traduite en HTTP 404 (côté API) ou en page d'erreur lisible (côté web).
 */
public class RessourceIntrouvableException extends ExceptionMetier {

    public RessourceIntrouvableException(String typeRessource, Integer identifiant) {
        super(typeRessource + " introuvable pour l'identifiant " + identifiant + ".");
    }
}
