package altn72.tpfilrouge.controleur;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Vérifie le rendu des pages (templates Thymeleaf) et l'application des droits
 * côté serveur (RG11). Base H2 en mémoire, utilisateurs simulés.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PagesWebTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void pageDeConnexionEstPublique() throws Exception {
        mockMvc.perform(get("/connexion")).andExpect(status().isOk());
    }

    @Test
    void pageProtegeeSansAuthentificationRedirigeVersLaConnexion() throws Exception {
        mockMvc.perform(get("/stations")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "agent", roles = "AGENT")
    void listeDesStationsEstAfficheePourUnAgent() throws Exception {
        mockMvc.perform(get("/stations")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "agent", roles = "AGENT")
    void tableauDeBordEstInterditAUnAgent() throws Exception {
        mockMvc.perform(get("/supervision")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "resp", roles = "RESPONSABLE_EXPLOITATION")
    void pagesDuResponsableSontAffichees() throws Exception {
        mockMvc.perform(get("/supervision")).andExpect(status().isOk());
        mockMvc.perform(get("/supervision/statistiques")).andExpect(status().isOk());
        mockMvc.perform(get("/historique")).andExpect(status().isOk());
        mockMvc.perform(get("/prestataires")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "resp", roles = "RESPONSABLE_EXPLOITATION")
    void identifiantNonNumeriqueAfficheLaPageDErreur() throws Exception {
        mockMvc.perform(get("/incidents/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"));
    }
}
