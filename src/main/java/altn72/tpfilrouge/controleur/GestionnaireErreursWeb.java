package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.exception.EvaluationInvalideException;
import altn72.tpfilrouge.exception.ExceptionMetier;
import altn72.tpfilrouge.exception.RessourceIntrouvableException;
import altn72.tpfilrouge.exception.TransitionIncidentInterditeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Traduit les exceptions métier et les adresses invalides en pages d'erreur lisibles côté web (§7.4).
 * Aucune trace technique n'est présentée à l'utilisateur : seul le message
 * métier (en français) est affiché, avec le bon code de statut HTTP. L'erreur est
 * journalisée ici, là où elle est traitée, et nulle part ailleurs.
 */
@ControllerAdvice(basePackages = "altn72.tpfilrouge.controleur")
public class GestionnaireErreursWeb {

    private static final Logger log = LoggerFactory.getLogger(GestionnaireErreursWeb.class);
    private static final String VUE_ERREUR = "error";

    @ExceptionHandler(RessourceIntrouvableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ressourceIntrouvable(RessourceIntrouvableException exception, Model model) {
        log.warn("Ressource introuvable : {}", exception.getMessage());
        model.addAttribute("message", exception.getMessage());
        return VUE_ERREUR;
    }

    @ExceptionHandler({TransitionIncidentInterditeException.class, EvaluationInvalideException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String conflit(ExceptionMetier exception, Model model) {
        log.warn("Opération refusée : {}", exception.getMessage());
        model.addAttribute("message", exception.getMessage());
        return VUE_ERREUR;
    }

    /** Identifiant non numérique dans l'URL (ex. /incidents/abc) : page lisible au lieu de l'erreur brute de Spring. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String parametreInvalide(MethodArgumentTypeMismatchException exception, Model model) {
        log.warn("Paramètre invalide « {} » : {}", exception.getName(), exception.getValue());
        model.addAttribute("message", "L'adresse demandée n'est pas valide : l'identifiant doit être un nombre.");
        return VUE_ERREUR;
    }
}
