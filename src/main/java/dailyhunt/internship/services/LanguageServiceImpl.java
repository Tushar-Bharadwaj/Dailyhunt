package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.LanguageIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllLanguages;
import dailyhunt.internship.clientmodels.response.NewsComponentsRequest;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.news.components.LanguageRepository;
import dailyhunt.internship.services.interfaces.LanguageService;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository,
                               WebClient.Builder webClientBuilder) {
        this.languageRepository = languageRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Language findLanguageById(Long id) {
        Optional<Language> language = languageRepository.findById(id);
        if(!language.isPresent())
            throw new ResourceNotFoundException("This Genre does not exist");
        return language.get();
    }

    @Override
    public AllLanguages findAllLanguage() {

        return AllLanguages.builder()
                .all_the_languages(languageRepository.findAll())
                .build();
    }

    @Override
    public AllLanguages findLanguagesByIds(LanguageIdList languageIdList) {
        return AllLanguages.builder()
                .all_the_languages(languageRepository.findAllById(languageIdList.getLanguageIds()))
                .build();
    }

    @Override
    public Language saveLanguage(NewsComponents language) {
        if(languageRepository.existsByName(language.getName()))
            throw new BadRequestException("There already exists a Language with same name");
        return languageRepository.save(
                Language.builder()
                        .active(true)
                        .name(StringUtils.capitalize(language.getName().toLowerCase()))
                        .postCount(0L)
                        .build()
        );
    }

    @Override
    public void saveLanguageAtUserProfile(Language language) {
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(language.getId())
                .name(language.getName())
                .build();
        String fooResourceUrl = "https://dailyhunt-user-profile.herokuapp.com/api/v1/injestion/user_profile/newsComponents/language";
        String result = webClientBuilder.build()
                .post()
                .uri(fooResourceUrl)
                .body(Mono.just(newsComponentsRequest), NewsComponentsRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Language updateLanguageName(NewsComponents request, Long languageId)
            throws BadRequestException, ResourceNotFoundException {
        Optional<Language> checkLanguage = languageRepository.findById(languageId);
        if(languageRepository.existsByName(request.getName()))
            throw new BadRequestException("Language with this name already exists");
        if(checkLanguage.isPresent()) {
            Language language = checkLanguage.get();
            language.setName(request.getName());
            return languageRepository.save(language);
        }
        throw new ResourceNotFoundException("Invalid language selected.");
    }

    @Override
    public Language updateLanguageActive(Long languageId) throws
            ResourceNotFoundException {
        Optional<Language> checkLanguage = languageRepository.findById(languageId);
        if(checkLanguage.isPresent()) {
            Language language = checkLanguage.get();
            language.setActive(!language.getActive());
            return languageRepository.save(language);
        }
        throw new ResourceNotFoundException("Invalid language selected");
    }

    @Override
    public void incrementLanguagePostCount(Language language) {
        language.setPostCount(language.getPostCount() + 1);
    }

    @Override
    public Boolean deleteLanguage(Long languageId) throws ResourceNotFoundException {
        Optional<Language> genre = languageRepository.findById(languageId);
        if(genre.isPresent()) {
            deleteLanguageAtUserProfile(languageId);
            languageRepository.deleteById(languageId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid Language selected");
    }

    public void deleteLanguageAtUserProfile(Long languageId) {
        Language language = findLanguageById(languageId);
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(language.getId())
                .name(language.getName())
                .build();
        String fooResourceUrl = "http://profile-service/api/v1/user_profile/newsComponents/language";
        String result = webClientBuilder.build()
                .delete()
                .uri(fooResourceUrl+"/"+languageId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public List<Language> findAllById(List<Long> ids) {

        return languageRepository.findAllById(ids);
    }
}
