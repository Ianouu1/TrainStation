package altn72.tpfilrouge.service;

import altn72.tpfilrouge.exception.EvaluationInvalideException;
import altn72.tpfilrouge.modele.Equipement;
import altn72.tpfilrouge.modele.EtatEquipement;
import altn72.tpfilrouge.modele.Evaluation;
import altn72.tpfilrouge.modele.EvaluationRepository;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.Prestataire;
import altn72.tpfilrouge.modele.Station;
import altn72.tpfilrouge.modele.StatutIncident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final LocalDateTime MAINTENANT = LocalDateTime.of(2026, 10, 14, 15, 0);

    @Mock private EvaluationRepository evaluationRepository;
    @Mock private IncidentService incidentService;

    private EvaluationService evaluationService;

    @BeforeEach
    void preparer() {
        Clock horloge = Clock.fixed(MAINTENANT.atZone(ZONE).toInstant(), ZONE);
        evaluationService = new EvaluationService(evaluationRepository, incidentService, horloge);
    }

    private Incident incident(StatutIncident statut, boolean avecPrestataire) {
        Equipement equipement = new Equipement("REF-1", "Portique", "PORTIQUE", EtatEquipement.EN_PANNE, new Station("Nation"));
        Incident incident = new Incident(equipement, "Panne", MAINTENANT.minusHours(50));
        incident.setStatut(statut);
        if (avecPrestataire) {
            incident.setPrestataire(new Prestataire("PortiQ", "sav@portiq.fr"));
        }
        return incident;
    }

    @Test
    void evaluerUnIncidentNonResoluEstRefuse() {
        when(incidentService.obtenirIncident(1)).thenReturn(incident(StatutIncident.AFFECTE, true));

        assertThatThrownBy(() -> evaluationService.evaluer(1, 4, "ok"))
                .isInstanceOf(EvaluationInvalideException.class);
    }

    @Test
    void evaluerUnIncidentSansPrestataireEstRefuse() {
        when(incidentService.obtenirIncident(1)).thenReturn(incident(StatutIncident.RESOLU, false));

        assertThatThrownBy(() -> evaluationService.evaluer(1, 4, "ok"))
                .isInstanceOf(EvaluationInvalideException.class);
    }

    @Test
    void evaluerAvecUneNoteHorsBornesEstRefuse() {
        when(incidentService.obtenirIncident(1)).thenReturn(incident(StatutIncident.RESOLU, true));

        assertThatThrownBy(() -> evaluationService.evaluer(1, 6, "trop"))
                .isInstanceOf(EvaluationInvalideException.class);
    }

    @Test
    void evaluerUnIncidentDejaEvalueEstRefuse() {
        when(incidentService.obtenirIncident(1)).thenReturn(incident(StatutIncident.RESOLU, true));
        when(evaluationRepository.existsByIncident_IdIncident(1)).thenReturn(true);

        assertThatThrownBy(() -> evaluationService.evaluer(1, 4, "ok"))
                .isInstanceOf(EvaluationInvalideException.class);
    }

    @Test
    void evaluationValideEstEnregistree() {
        when(incidentService.obtenirIncident(1)).thenReturn(incident(StatutIncident.RESOLU, true));
        when(evaluationRepository.existsByIncident_IdIncident(1)).thenReturn(false);
        when(evaluationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Evaluation evaluation = evaluationService.evaluer(1, 4, "Rapide");

        assertThat(evaluation.getNote()).isEqualTo(4);
        assertThat(evaluation.getDateEvaluation()).isEqualTo(MAINTENANT);
    }
}
