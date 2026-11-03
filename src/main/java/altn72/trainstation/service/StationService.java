package altn72.trainstation.service;

import altn72.trainstation.exception.RessourceIntrouvableException;
import altn72.trainstation.modele.Station;
import altn72.trainstation.modele.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cas d'usage liés aux stations.
 */
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> listerStations() {
        return stationRepository.findAllByOrderByNomAsc();
    }

    public Station obtenirStation(Integer idStation) {
        return stationRepository.findById(idStation)
                .orElseThrow(() -> new RessourceIntrouvableException("Station", idStation));
    }
}
