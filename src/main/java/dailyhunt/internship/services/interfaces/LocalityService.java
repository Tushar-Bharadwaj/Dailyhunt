package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;

public interface LocalityService {
    Locality findLocalityById(Long id);

    List<Locality> findAllLocalities();

    Locality saveLocality(NewsComponents request);

    Locality updateLocality(NewsComponents request, Long localityId) throws ResourceNotFoundException;

    Boolean deleteLocality(Long localityId) throws ResourceNotFoundException;

    List<Locality> findAllById(List<Long> id);

    Locality toggleActiveStatus(Long localityId);
}
