package altn72.tpfilrouge.api;

import altn72.tpfilrouge.dto.IncidentApiDto;
import altn72.tpfilrouge.service.ApiIncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST des incidents (F12) : consultation et modification d'état.
 *
 * <p>{@code ResponseEntity} est utilisé ici car l'API doit maîtriser finement le
 * code de statut (200, 201, 404 via le gestionnaire d'erreurs), usage pertinent
 * contrairement aux pages web qui redirigent.</p>
 */
@RestController
@RequestMapping("/api/incidents")
@Tag(name = "Incidents", description = "Consultation et évolution de l'état des incidents")
public class IncidentApiControleur {

    private final ApiIncidentService apiIncidentService;

    public IncidentApiControleur(ApiIncidentService apiIncidentService) {
        this.apiIncidentService = apiIncidentService;
    }

    @GetMapping
    @Operation(summary = "Lister tous les incidents")
    public List<IncidentApiDto> lister() {
        return apiIncidentService.lister();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un incident par son identifiant")
    public ResponseEntity<IncidentApiDto> obtenir(@PathVariable Integer id) {
        return ResponseEntity.ok(apiIncidentService.obtenir(id));
    }

    @PostMapping("/{id}/resolution")
    @Operation(summary = "Déclarer un incident comme résolu")
    public ResponseEntity<IncidentApiDto> resoudre(@PathVariable Integer id) {
        return ResponseEntity.ok(apiIncidentService.resoudre(id));
    }

    @PostMapping("/{id}/affectation")
    @Operation(summary = "Affecter ou réaffecter un prestataire à un incident")
    public ResponseEntity<IncidentApiDto> affecter(@PathVariable Integer id,
                                                    @RequestParam Integer idPrestataire) {
        return ResponseEntity.ok(apiIncidentService.affecter(id, idPrestataire));
    }
}
