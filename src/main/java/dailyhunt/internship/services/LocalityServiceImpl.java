package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.LocalityIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllLanguages;
import dailyhunt.internship.clientmodels.response.AllLocalities;
import dailyhunt.internship.clientmodels.response.NewsComponentsRequest;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.news.components.LocalityRepository;
import dailyhunt.internship.services.interfaces.LocalityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class LocalityServiceImpl implements LocalityService {

    private final LocalityRepository localityRepository;
    private  final WebClient.Builder webClientBuilder;

    @Autowired
    public LocalityServiceImpl(LocalityRepository localityRepository,
                               WebClient.Builder webClientBuilder) {
        this.localityRepository = localityRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Locality findLocalityById(Long id) {
        Optional<Locality> locality = localityRepository.findById(id);
        if(!locality.isPresent())
            throw new ResourceNotFoundException("This Locality does not exist");
        return locality.get();
    }

    @Override
    public AllLocalities findAllLocalities() {
        return AllLocalities.builder()
                .all_the_localities(localityRepository.findAll())
                .build();
    }

    @Override
    public AllLocalities findLocalitiesByIds(LocalityIdList localityIdList) {
        return AllLocalities.builder()
                .all_the_localities(localityRepository.findAllById(localityIdList.getLocalityIds()))
                .build();

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
    public void saveLocalityAtUserProfile(Locality locality) {
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(locality.getId())
                .name(locality.getName())
                .build();
        String fooResourceUrl = "https://dailyhunt-user-profile.herokuapp.com/api/v1/user_profile/newsComponents/locality";
        String result = webClientBuilder.build()
                .post()
                .uri(fooResourceUrl)
                .body(Mono.just(newsComponentsRequest), NewsComponentsRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Locality updateLocalityName(NewsComponents request, Long localityId) throws ResourceNotFoundException {
        Optional<Locality> checkLocality = localityRepository.findById(localityId);
        if(localityRepository.existsByName(request.getName()))
            throw new BadRequestException("Locality with this name already exist");
        if(checkLocality.isPresent()) {
            Locality locality = checkLocality.get();
            locality.setName(request.getName());
            return localityRepository.save(locality);
        }
        throw new ResourceNotFoundException("Invalid locality selected.");
    }

    @Override
    public Locality updateLocalityActive(Long localityId) throws ResourceNotFoundException {
        Optional<Locality> checkLocality = localityRepository.findById(localityId);
        if(checkLocality.isPresent()) {
            Locality locality = checkLocality.get();
            locality.setActive(!locality.getActive());
            return localityRepository.save(locality);
        }
        throw new ResourceNotFoundException("Invalid locality selected.");
    }

    @Override
    public void incrementLocalityPostCount(Locality locality) {
        locality.setPostCount(locality.getPostCount() + 1);
    }

    @Override
    public Boolean deleteLocality(Long localityId) throws ResourceNotFoundException {
        Optional<Locality> locality = localityRepository.findById(localityId);
        if(locality.isPresent()) {
            deleteLocalityAtUserProfile(localityId);
            localityRepository.deleteById(localityId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid locality selected");
    }

    public void deleteLocalityAtUserProfile(Long localityId) {
        Locality locality = findLocalityById(localityId);
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(locality.getId())
                .name(locality.getName())
                .build();
        String fooResourceUrl = "http://profile-service/api/v1/user_profile/newsComponents/locality";
        String result = webClientBuilder.build()
                .delete()
                .uri(fooResourceUrl+"/"+localityId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public List<Locality> findAllById(List<Long> ids) {
        return localityRepository.findAllById(ids);
    }

    @Override
    public Optional<Locality> findLocalityByName(String name){
        return localityRepository.findByName(name);
    }
}
