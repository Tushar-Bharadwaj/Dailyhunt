package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.LocalityIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllLocalities;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface LocalityService {
    Locality findLocalityById(Long id);

    AllLocalities findAllLocalities();

    AllLocalities findLocalitiesByIds(LocalityIdList localityIdList);

    Locality saveLocality(NewsComponents request);

    void saveLocalityAtUserProfile(Locality locality);

    Locality updateLocalityName(NewsComponents request, Long localityId)
            throws BadRequestException, ResourceNotFoundException;

    Locality updateLocalityActive(Long localityId) throws
            ResourceNotFoundException;

    void incrementLocalityPostCount(Locality locality);

    Boolean deleteLocality(Long localityId) throws ResourceNotFoundException;

    List<Locality> findAllById(List<Long> id);

    Optional<Locality> findLocalityByName(String name);
}
