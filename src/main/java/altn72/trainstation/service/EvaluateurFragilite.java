package altn72.trainstation.service;

import altn72.trainstation.modele.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Détermine si un équipement est fragile : au moins 3 incidents ouverts durant
 * les 30 derniers jours, quel que soit leur statut (EF1 à EF3).
 *
 * <p>La fenêtre est glissante et recalculée à chaque consultation à partir de
 * l'horloge injectée ; le caractère fragile n'est jamais enregistré en base (EF4).
 * Le seuil et la fenêtre sont définis ici, à un seul endroit (EF5), et la règle
 * vit dans la couche service, jamais dans un contrôleur ni une vue (EF6).</p>
 */
@Service
public class EvaluateurFragilite {

    /** Nombre d'incidents à partir duquel un équipement est fragile, 3 inclus (EF3). */
    public static final int SEUIL_INCIDENTS_FRAGILITE = 3;

    /** Durée de la fenêtre glissante d'observation, en jours (EF1). */
    public static final int FENETRE_FRAGILITE_EN_JOURS = 30;

    private final IncidentRepository incidentRepository;
    private final Clock horloge;

    public EvaluateurFragilite(IncidentRepository incidentRepository, Clock horloge) {
        this.incidentRepository = incidentRepository;
        this.horloge = horloge;
    }

    /** Vrai si l'équipement a au moins 3 incidents ouverts dans la fenêtre de 30 jours (EF3). */
    @Transactional(readOnly = true)
    public boolean estFragile(Integer idEquipement) {
        return compterIncidentsDansLaFenetre(idEquipement) >= SEUIL_INCIDENTS_FRAGILITE;
    }

    /** Nombre d'incidents de l'équipement ouverts depuis (heure courante − 30 jours), incluse (EF1, EF2). */
    @Transactional(readOnly = true)
    public long compterIncidentsDansLaFenetre(Integer idEquipement) {
        return incidentRepository.countByEquipement_IdEquipementAndDateOuvertureGreaterThanEqual(
                idEquipement, calculerDebutFenetre());
    }

    /** Identifiants de tous les équipements fragiles à l'instant de la consultation (tableau de bord). */
    @Transactional(readOnly = true)
    public Set<Integer> listerEquipementsFragiles() {
        return new HashSet<>(incidentRepository.trouverEquipementsAvecAuMoinsNIncidentsDepuis(
                calculerDebutFenetre(), SEUIL_INCIDENTS_FRAGILITE));
    }

    private LocalDateTime calculerDebutFenetre() {
        return LocalDateTime.now(horloge).minusDays(FENETRE_FRAGILITE_EN_JOURS);
    }
}
