package altn72.trainstation.controleur;

import altn72.trainstation.modele.Equipement;
import altn72.trainstation.modele.EquipementRepository;
import altn72.trainstation.modele.EtatEquipement;
import altn72.trainstation.modele.Incident;
import altn72.trainstation.modele.IncidentRepository;
import altn72.trainstation.modele.Station;
import altn72.trainstation.modele.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Contrat d'interface de l'évolution « Équipement fragile » : badge sur la fiche
 * équipement, badge sur le tableau de bord et message de confirmation de la
 * déclaration. Base H2 en mémoire, données annulées après chaque test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "resp", roles = "RESPONSABLE_EXPLOITATION")
class EquipementFragileWebTest {

    private static final String BADGE = "Équipement fragile";

    @Autowired private MockMvc mockMvc;
    @Autowired private StationRepository stationRepository;
    @Autowired private EquipementRepository equipementRepository;
    @Autowired private IncidentRepository incidentRepository;

    private Equipement equipementFragile;
    private Equipement equipementVoisin;

    @BeforeEach
    void preparerJeuDeDonnees() {
        Station station = stationRepository.save(new Station("Gare du Nord"));
        equipementFragile = equipementRepository.save(
                new Equipement("FRAG-1", "Ascenseur quai 1", "ASCENSEUR", EtatEquipement.EN_PANNE, station));
        equipementVoisin = equipementRepository.save(
                new Equipement("FRAG-2", "Escalier quai 2", "ESCALIER", EtatEquipement.EN_SERVICE, station));
        LocalDateTime maintenant = LocalDateTime.now();
        for (int jours = 1; jours <= 3; jours++) {
            incidentRepository.save(new Incident(equipementFragile, "Panne récurrente", maintenant.minusDays(jours)));
        }
        incidentRepository.save(new Incident(equipementVoisin, "Panne isolée", maintenant.minusDays(1)));
    }

    @Test
    void ficheDunEquipementFragileAfficheLeBadge() throws Exception {
        mockMvc.perform(get("/equipements/{id}", equipementFragile.getIdEquipement()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(BADGE)));
    }

    @Test
    void ficheDunEquipementNonFragileNAffichePasLeBadge() throws Exception {
        mockMvc.perform(get("/equipements/{id}", equipementVoisin.getIdEquipement()))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString(BADGE))));
    }

    @Test
    void tableauDeBordAfficheLeBadgeSurLesIncidentsDeLEquipementFragile() throws Exception {
        mockMvc.perform(get("/supervision"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(BADGE)));
    }

    @Test
    void declarationQuiRendLEquipementFragileLeSignaleDansLaConfirmation() throws Exception {
        incidentRepository.save(new Incident(equipementVoisin, "Deuxième panne", LocalDateTime.now().minusHours(2)));

        mockMvc.perform(post("/equipements/{id}/incidents", equipementVoisin.getIdEquipement())
                        .param("description", "Troisième panne de l'escalier")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("messageConfirmation", containsString("fragile")));
    }

    @Test
    void declarationSurUnEquipementNonFragileNeMentionnePasLaFragilite() throws Exception {
        mockMvc.perform(post("/equipements/{id}/incidents", equipementVoisin.getIdEquipement())
                        .param("description", "Deuxième panne de l'escalier")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("messageConfirmation", not(containsString("fragile"))));
    }
}
