package altn72.tpfilrouge.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de l'API REST (F12), base H2 en mémoire.
 */
@SpringBootTest
@AutoConfigureMockMvc
class IncidentApiControleurTest {

    @Autowired private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "RESPONSABLE_EXPLOITATION")
    void listerLesIncidentsRepond200() throws Exception {
        mockMvc.perform(get("/api/incidents")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "RESPONSABLE_EXPLOITATION")
    void incidentInexistantRepond404() throws Exception {
        mockMvc.perform(get("/api/incidents/999999")).andExpect(status().isNotFound());
    }

    @Test
    void apiSansAuthentificationEstRefusee() throws Exception {
        mockMvc.perform(get("/api/incidents")).andExpect(status().is3xxRedirection());
    }
}
