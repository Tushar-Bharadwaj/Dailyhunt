package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.LanguageIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllLanguages;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface LanguageService {
    Language findLanguageById(Long id);

    AllLanguages findAllLanguage();

    AllLanguages findLanguagesByIds(LanguageIdList languageIdList);

    Language saveLanguage(NewsComponents language);

    void saveLanguageAtUserProfile(Language language);

    Language updateLanguageName(NewsComponents request, Long languageId)
            throws BadRequestException, ResourceNotFoundException;

    Language updateLanguageActive(Long languageId) throws
            ResourceNotFoundException;

    void incrementLanguagePostCount(Language language);

    Boolean deleteLanguage(Long languageId) throws ResourceNotFoundException;

    List<Language> findAllById(List<Long> ids);
}

