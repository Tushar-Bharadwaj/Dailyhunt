package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface LocalityService {
    Locality findLocalityById(Long id);

    List<Locality> findAllLocalities();

    Locality saveLocality(NewsComponents request);

    Locality updateLocality(NewsComponents request, Long localityId) throws ResourceNotFoundException;

    Boolean deleteLocality(Long localityId) throws ResourceNotFoundException;

    List<Locality> findAllById(List<Long> id);

    Optional<Locality> findLocalityByName(String name);
}
