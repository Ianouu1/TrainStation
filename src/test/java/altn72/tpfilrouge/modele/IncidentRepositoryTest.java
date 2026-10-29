package altn72.tpfilrouge.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests d'intégration de la couche persistance (base H2 en mémoire).
 */
@DataJpaTest
class IncidentRepositoryTest {

    @Autowired private StationRepository stationRepository;
    @Autowired private EquipementRepository equipementRepository;
    @Autowired private IncidentRepository incidentRepository;

    private Equipement equipement;

    @BeforeEach
    void preparerJeuDeDonnees() {
        Station station = stationRepository.save(new Station("Châtelet"));
        equipement = equipementRepository.save(
                new Equipement("REF-1", "Ascenseur", "ASCENSEUR", EtatEquipement.EN_PANNE, station));

        Incident actif = new Incident(equipement, "Actif", LocalDateTime.now().minusHours(10));

        Incident resoluRecent = new Incident(equipement, "Résolu récent", LocalDateTime.now().minusHours(50));
        resoluRecent.setStatut(StatutIncident.RESOLU);
        resoluRecent.setDateResolution(LocalDateTime.now().minusHours(2));

        Incident resoluAncien = new Incident(equipement, "Résolu ancien", LocalDateTime.now().minusHours(100));
        resoluAncien.setStatut(StatutIncident.RESOLU);
        resoluAncien.setDateResolution(LocalDateTime.now().minusHours(40));

        incidentRepository.saveAll(List.of(actif, resoluRecent, resoluAncien));
    }

    @Test
    void incidentsActifsExcluentLesIncidentsResolus() {
        List<Incident> actifs = incidentRepository.trouverIncidentsActifs();
        assertThat(actifs).hasSize(1);
        assertThat(actifs.get(0).getStatut()).isEqualTo(StatutIncident.NON_AFFECTE);
    }

    @Test
    void historiqueEstTrieDuPlusRecentAuPlusAncien() {
        List<Incident> historique =
                incidentRepository.findByStatutOrderByDateResolutionDesc(StatutIncident.RESOLU);
        assertThat(historique).hasSize(2);
        assertThat(historique.get(0).getDateResolution())
                .isAfter(historique.get(1).getDateResolution());
    }

    @Test
    void requeteNativeCompteLesIncidentsActifsParStation() {
        List<Object[]> parStation = incidentRepository.compterIncidentsActifsParStation();
        assertThat(parStation).hasSize(1);
        assertThat(String.valueOf(parStation.get(0)[0])).isEqualTo("Châtelet");
        assertThat(((Number) parStation.get(0)[1]).intValue()).isEqualTo(1);
    }
}
