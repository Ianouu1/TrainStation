package altn72.tpfilrouge.service;

import altn72.tpfilrouge.dto.CritereSupervisionForm;
import altn72.tpfilrouge.dto.LigneSupervisionDto;
import altn72.tpfilrouge.modele.Equipement;
import altn72.tpfilrouge.modele.EtatEquipement;
import altn72.tpfilrouge.modele.Incident;
import altn72.tpfilrouge.modele.NiveauUrgence;
import altn72.tpfilrouge.modele.StatutIncident;
import altn72.tpfilrouge.modele.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupervisionServiceTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final LocalDateTime MAINTENANT = LocalDateTime.of(2026, 10, 14, 15, 0);

    @Mock private IncidentService incidentService;
    private SupervisionService supervisionService;

    @BeforeEach
    void preparer() {
        Clock horloge = Clock.fixed(MAINTENANT.atZone(ZONE).toInstant(), ZONE);
        supervisionService = new SupervisionService(incidentService, new EvaluateurUrgence(horloge));
    }

    private Incident incident(int idStation, String nomStation, long heures, StatutIncident statut) {
        Station station = new Station(nomStation);
        station.setIdStation(idStation);
        Equipement equipement = new Equipement("REF-" + idStation, "Eq", "TYPE", EtatEquipement.EN_PANNE, station);
        Incident incident = new Incident(equipement, "Panne", MAINTENANT.minusHours(heures));
        incident.setStatut(statut);
        return incident;
    }

    @Test
    void incidentAffecteNaPasDeCouleurEtLesAutresSuiventLeurAnciennete() {
        when(incidentService.listerIncidentsActifs()).thenReturn(List.of(
                incident(1, "A", 10, StatutIncident.NON_AFFECTE),   // vert
                incident(1, "A", 50, StatutIncident.NON_AFFECTE),   // orange
                incident(1, "A", 80, StatutIncident.NON_AFFECTE),   // rouge
                incident(1, "A", 60, StatutIncident.AFFECTE)));     // affecté -> pas d'indicateur

        List<LigneSupervisionDto> lignes = supervisionService.construireTableauDeBord(new CritereSupervisionForm(null, null, null));

        // Tri par défaut : urgence décroissante puis ancienneté -> rouge, orange, vert, affecté
        assertThat(lignes.get(0).niveauUrgence()).isEqualTo(NiveauUrgence.ROUGE);
        assertThat(lignes.get(1).niveauUrgence()).isEqualTo(NiveauUrgence.ORANGE);
        assertThat(lignes.get(2).niveauUrgence()).isEqualTo(NiveauUrgence.VERT);
        assertThat(lignes.get(3).estAffecte()).isTrue();
        assertThat(lignes.get(3).niveauUrgence()).isNull();
    }

    @Test
    void filtreParNiveauRougeNeRenvoieQueLesRouges() {
        when(incidentService.listerIncidentsActifs()).thenReturn(List.of(
                incident(1, "A", 10, StatutIncident.NON_AFFECTE),
                incident(1, "A", 80, StatutIncident.NON_AFFECTE)));

        List<LigneSupervisionDto> lignes = supervisionService.construireTableauDeBord(
                new CritereSupervisionForm(null, "ROUGE", null));

        assertThat(lignes).hasSize(1);
        assertThat(lignes.get(0).niveauUrgence()).isEqualTo(NiveauUrgence.ROUGE);
    }

    @Test
    void filtreParStationNeRenvoieQueLaStationDemandee() {
        when(incidentService.listerIncidentsActifs()).thenReturn(List.of(
                incident(1, "A", 10, StatutIncident.NON_AFFECTE),
                incident(2, "B", 10, StatutIncident.NON_AFFECTE)));

        List<LigneSupervisionDto> lignes = supervisionService.construireTableauDeBord(
                new CritereSupervisionForm(2, null, null));

        assertThat(lignes).hasSize(1);
        assertThat(lignes.get(0).incident().getEquipement().getStation().getNom()).isEqualTo("B");
    }
}
