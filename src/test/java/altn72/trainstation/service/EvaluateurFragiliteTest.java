package altn72.trainstation.service;

import altn72.trainstation.modele.Equipement;
import altn72.trainstation.modele.EquipementRepository;
import altn72.trainstation.modele.EtatEquipement;
import altn72.trainstation.modele.Incident;
import altn72.trainstation.modele.IncidentRepository;
import altn72.trainstation.modele.Station;
import altn72.trainstation.modele.StationRepository;
import altn72.trainstation.modele.StatutIncident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Règle « équipement fragile » (EF1 à EF4) vérifiée sur les cas limites de
 * référence de l'énoncé. Base H2 en mémoire et horloge figée : le comptage se
 * fait réellement en base, bornes de la fenêtre comprises.
 */
@DataJpaTest
class EvaluateurFragiliteTest {

    private static final ZoneId ZONE = ZoneId.of("Europe/Paris");
    private static final LocalDateTime MAINTENANT = LocalDateTime.of(2026, 11, 5, 14, 0);

    @Autowired private StationRepository stationRepository;
    @Autowired private EquipementRepository equipementRepository;
    @Autowired private IncidentRepository incidentRepository;

    private EvaluateurFragilite evaluateurFragilite;
    private Station station;
    private Equipement equipement;

    @BeforeEach
    void preparer() {
        Clock horloge = Clock.fixed(MAINTENANT.atZone(ZONE).toInstant(), ZONE);
        evaluateurFragilite = new EvaluateurFragilite(incidentRepository, horloge);
        station = stationRepository.save(new Station("Châtelet"));
        equipement = creerEquipement("REF-1");
    }

    private Equipement creerEquipement(String reference) {
        return equipementRepository.save(
                new Equipement(reference, "Ascenseur", "ASCENSEUR", EtatEquipement.EN_SERVICE, station));
    }

    private void ouvrirIncident(Equipement cible, LocalDateTime dateOuverture, StatutIncident statut) {
        Incident incident = new Incident(cible, "Panne constatée", dateOuverture);
        incident.setStatut(statut);
        if (statut == StatutIncident.RESOLU) {
            incident.setDateResolution(dateOuverture.plusHours(1));
        }
        incidentRepository.save(incident);
    }

    private void ouvrirIncidentNonAffecte(Equipement cible, LocalDateTime dateOuverture) {
        ouvrirIncident(cible, dateOuverture, StatutIncident.NON_AFFECTE);
    }

    @Test
    void deuxIncidentsDansLaFenetreNeRendentPasFragile() {
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(1));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(10));

        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isFalse();
    }

    @Test
    void troisIncidentsDansLaFenetreRendentFragile() {
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(1));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(10));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(20));

        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isTrue();
    }

    @Test
    void unIncidentResoluCompteAussi() {
        ouvrirIncident(equipement, MAINTENANT.minusDays(1), StatutIncident.NON_AFFECTE);
        ouvrirIncident(equipement, MAINTENANT.minusDays(2), StatutIncident.AFFECTE);
        ouvrirIncident(equipement, MAINTENANT.minusDays(3), StatutIncident.RESOLU);

        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isTrue();
    }

    @Test
    void incidentOuvertIlYATrenteJoursEtUneMinuteNestPasCompte() {
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(1));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(2));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(30).minusMinutes(1));

        assertThat(evaluateurFragilite.compterIncidentsDansLaFenetre(equipement.getIdEquipement())).isEqualTo(2);
        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isFalse();
    }

    @Test
    void incidentOuvertIlYAVingtNeufJoursVingtTroisHeuresCinquanteNeufEstCompte() {
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(1));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(2));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(29).minusHours(23).minusMinutes(59));

        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isTrue();
    }

    @Test
    void incidentOuvertPileATrenteJoursEstCompte() {
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(1));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(2));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(30));

        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isTrue();
    }

    @Test
    void incidentsVieuxDeQuaranteCinqJoursNeComptentPas() {
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(1));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(2));
        for (int i = 0; i < 5; i++) {
            ouvrirIncident(equipement, MAINTENANT.minusDays(45), StatutIncident.RESOLU);
        }

        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isFalse();
    }

    @Test
    void lEquipementVoisinNestPasContamine() {
        Equipement voisin = creerEquipement("REF-2");
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(1));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(2));
        ouvrirIncidentNonAffecte(equipement, MAINTENANT.minusDays(3));
        ouvrirIncidentNonAffecte(voisin, MAINTENANT.minusDays(1));

        assertThat(evaluateurFragilite.estFragile(equipement.getIdEquipement())).isTrue();
        assertThat(evaluateurFragilite.estFragile(voisin.getIdEquipement())).isFalse();
        assertThat(evaluateurFragilite.listerEquipementsFragiles())
                .containsExactly(equipement.getIdEquipement());
    }
}
