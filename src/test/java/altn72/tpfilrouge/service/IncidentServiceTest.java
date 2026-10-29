package altn72.tpfilrouge.service;

import altn72.tpfilrouge.exception.TransitionIncidentInterditeException;
import altn72.tpfilrouge.modele.Equipement;
import altn72.tpfilrouge.modele.EquipementRepository;
import altn72.tpfilrouge.modele.EtatEquipement;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.IncidentRepository;
import altn72.tpfilrouge.modele.Prestataire;
import altn72.tpfilrouge.modele.PrestataireRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final LocalDateTime MAINTENANT = LocalDateTime.of(2026, 10, 14, 15, 0);

    @Mock private IncidentRepository incidentRepository;
    @Mock private EquipementRepository equipementRepository;
    @Mock private PrestataireRepository prestataireRepository;

    private IncidentService incidentService;

    @BeforeEach
    void preparer() {
        Clock horloge = Clock.fixed(MAINTENANT.atZone(ZONE).toInstant(), ZONE);
        incidentService = new IncidentService(incidentRepository, equipementRepository, prestataireRepository, horloge);
    }

    private Equipement equipement() {
        return new Equipement("REF-1", "Ascenseur", "ASCENSEUR", EtatEquipement.EN_PANNE, new Station("Châtelet"));
    }

    @Test
    void incidentDeclareDemarreNonAffecteSansPrestataire() {
        when(equipementRepository.findById(1)).thenReturn(Optional.of(equipement()));
        when(incidentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Incident incident = incidentService.declarerIncident(1, "Panne");

        assertThat(incident.getStatut()).isEqualTo(StatutIncident.NON_AFFECTE);
        assertThat(incident.getPrestataire()).isNull();
        assertThat(incident.getDateOuverture()).isEqualTo(MAINTENANT);
    }

    @Test
    void affecterUnIncidentResoluEstInterdit() {
        Incident resolu = new Incident(equipement(), "Panne", MAINTENANT.minusHours(10));
        resolu.setStatut(StatutIncident.RESOLU);
        when(incidentRepository.findById(5)).thenReturn(Optional.of(resolu));

        assertThatThrownBy(() -> incidentService.affecterPrestataire(5, 2))
                .isInstanceOf(TransitionIncidentInterditeException.class);
    }

    @Test
    void affecterUnPrestatairePasseLIncidentEnAffecte() {
        Incident incident = new Incident(equipement(), "Panne", MAINTENANT.minusHours(10));
        when(incidentRepository.findById(5)).thenReturn(Optional.of(incident));
        when(prestataireRepository.findById(2)).thenReturn(Optional.of(new Prestataire("PortiQ", "sav@portiq.fr")));
        when(incidentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Incident resultat = incidentService.affecterPrestataire(5, 2);

        assertThat(resultat.getStatut()).isEqualTo(StatutIncident.AFFECTE);
        assertThat(resultat.getPrestataire()).isNotNull();
    }

    @Test
    void resoudreUnIncidentEnregistreSaDateEtSonStatut() {
        Incident incident = new Incident(equipement(), "Panne", MAINTENANT.minusHours(10));
        when(incidentRepository.findById(5)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Incident resultat = incidentService.resoudreIncident(5);

        assertThat(resultat.getStatut()).isEqualTo(StatutIncident.RESOLU);
        assertThat(resultat.getDateResolution()).isEqualTo(MAINTENANT);
    }

    @Test
    void resoudreUnIncidentDejaResoluEstInterdit() {
        Incident resolu = new Incident(equipement(), "Panne", MAINTENANT.minusHours(10));
        resolu.setStatut(StatutIncident.RESOLU);
        when(incidentRepository.findById(5)).thenReturn(Optional.of(resolu));

        assertThatThrownBy(() -> incidentService.resoudreIncident(5))
                .isInstanceOf(TransitionIncidentInterditeException.class);
    }
}
