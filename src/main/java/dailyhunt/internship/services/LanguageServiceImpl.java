package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.news.components.LanguageRepository;
import dailyhunt.internship.services.interfaces.LanguageService;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }


    @Override
    public Language findLanguageById(Long id) {
        Optional<Language> language = languageRepository.findById(id);
        if(!language.isPresent())
            throw new ResourceNotFoundException("This Genre does not exist");
        return language.get();
    }

    @Override
    public List<Language> findAllLanguage() {
        return languageRepository.findAll();
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
    public Language updateLanguage(NewsComponents request, Long languageId) throws ResourceNotFoundException {
        Language language = findLanguageById(languageId);
        language.setName(request.getName());
        return languageRepository.save(language);

    }

    @Override
    public Boolean deleteLanguage(Long languageId) throws ResourceNotFoundException {
        Optional<Language> genre = languageRepository.findById(languageId);
        if(genre.isPresent()) {
            languageRepository.deleteById(languageId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid Language selected");
    }

    @Override
    public List<Language> findAllById(List<Long> ids) {
        return languageRepository.findAllById(ids);
    }

    @Override
    public Language toggleActiveStatus(Long languageId) {
        Language language = findLanguageById(languageId);
        language.setActive(!language.getActive());
        languageRepository.save(language);
        return language;
    }
}
