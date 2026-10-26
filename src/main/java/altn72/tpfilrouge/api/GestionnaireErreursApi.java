package altn72.tpfilrouge.api;

import altn72.tpfilrouge.dto.MessageErreurDto;
import altn72.tpfilrouge.exception.EvaluationInvalideException;
import altn72.tpfilrouge.exception.ExceptionMetier;
import altn72.tpfilrouge.exception.RessourceIntrouvableException;
import altn72.tpfilrouge.exception.TransitionIncidentInterditeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduit les exceptions métier en réponses JSON de l'API REST avec le bon code
 * HTTP (F12, §7.4) : 404 pour une ressource inexistante, 409 pour un conflit
 * d'état. Aucune trace technique n'est renvoyée au client ; l'erreur est
 * journalisée ici, là où elle est traitée.
 */
@RestControllerAdvice(basePackages = "altn72.tpfilrouge.api")
public class GestionnaireErreursApi {

    private static final Logger log = LoggerFactory.getLogger(GestionnaireErreursApi.class);

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<MessageErreurDto> ressourceIntrouvable(RessourceIntrouvableException exception) {
        log.warn("API, ressource introuvable : {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageErreurDto(exception.getMessage()));
    }

    @ExceptionHandler({TransitionIncidentInterditeException.class, EvaluationInvalideException.class})
    public ResponseEntity<MessageErreurDto> conflit(ExceptionMetier exception) {
        log.warn("API, opération refusée : {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageErreurDto(exception.getMessage()));
    }
}
