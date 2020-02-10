package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.news.components.LocalityRepository;
import dailyhunt.internship.services.interfaces.LocalityService;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class LocalityServiceImpl implements LocalityService {
    private final LocalityRepository localityRepository;

    public LocalityServiceImpl(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
    }

    @Override
    public Locality findLocalityById(Long id) {
        Optional<Locality> locality = localityRepository.findById(id);
        if(!locality.isPresent())
            throw new ResourceNotFoundException("This Locality does not exist");
        return locality.get();
    }

    @Override
    public List<Locality> findAllLocalities() {
        return localityRepository.findAll();
    }

    @Override
    public Locality saveLocality(NewsComponents request) {
        if(localityRepository.existsByName(request.getName()))
            throw new BadRequestException("There already exists a Locality with same name");
        return localityRepository.save(
                Locality.builder()
                        .active(true)
                        .name(StringUtils.capitalize(request.getName().toLowerCase()))
                        .postCount(0L)
                        .build()
        );
    }

    @Override
    public Locality updateLocality(NewsComponents request, Long localityId) throws ResourceNotFoundException {
        Optional<Locality> checkLocality = localityRepository.findById(localityId);
        if(checkLocality.isPresent()) {
            if(DailyhuntUtil.isNullOrEmpty(request.getActive()))
                throw new BadRequestException("Please fill all fields");
            Locality locality = checkLocality.get();
            locality.setActive(request.getActive());
            return localityRepository.save(locality);
        }
        throw new ResourceNotFoundException("Invalid locality selected.");
    }

    @Override
    public Boolean deleteLocality(Long localityId) throws ResourceNotFoundException {
        Optional<Locality> locality = localityRepository.findById(localityId);
        if(locality.isPresent()) {
            localityRepository.deleteById(localityId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid locality selected");
    }

    @Override
    public List<Locality> findAllById(List<Long> id) {
        return null;
    }
}
