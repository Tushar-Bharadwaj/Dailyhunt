package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.SourceDTO;
import dailyhunt.internship.clientmodels.response.AllSources;
import dailyhunt.internship.clientmodels.response.NewsComponentsRequest;
import dailyhunt.internship.clientmodels.response.SourceRequest;
import dailyhunt.internship.entities.Source;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.SourceRepository;
import dailyhunt.internship.services.interfaces.ImageService;
import dailyhunt.internship.services.interfaces.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;
    private final ImageService imageService;
    private final WebClient.Builder webClientBuilder;


    @Autowired
    public SourceServiceImpl(SourceRepository sourceRepository, ImageService imageService,
                             WebClient.Builder webClientBuilder) {
        this.sourceRepository = sourceRepository;
        this.imageService = imageService;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public AllSources getAllSources() {
        return AllSources.builder()
                .all_the_sources(sourceRepository.findAll())
                .build();
    }

    @Override
    public Source save(SourceDTO sourceDTO) throws IOException {
        if(sourceRepository.existsByName(sourceDTO.getName()))
            throw new BadRequestException("There already exists a Source with same name");
        String path = imageService.saveImage(sourceDTO.getBase64());
        return sourceRepository.save(Source.builder()
                                .name(sourceDTO.getName())
                                .imagepath(path)
                                .build());
    }

    @Override
    public void saveSourceAtUserProfile(Source source) {
        SourceRequest sourceRequest = SourceRequest.builder()
                .id(source.getId())
                .name(source.getName())
                .imagepath(source.getImagepath())
                .build();
        String fooResourceUrl = "https://dailyhunt-user-profile.herokuapp.com/api/v1/injestion/user_profile/newsComponents/source";
        String result = webClientBuilder.build()
                .post()
                .uri(fooResourceUrl)
                .body(Mono.just(sourceRequest), SourceRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Source> source = sourceRepository.findById(id);
        if (source.isPresent()) {
            deleteSourceAtUserProfile(id);
            sourceRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Invalid source selected");
        }
    }

    public void deleteSourceAtUserProfile(Long id) {
        String fooResourceUrl = "https://dailyhunt-user-profile.herokuapp.com/api/v1/injestion/user_profile/newsComponents/source";
        String result = webClientBuilder.build()
                .delete()
                .uri(fooResourceUrl+"/"+id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
//     restTemplate.delete(fooResourceUrl+"/"+genreId);
    }

}
