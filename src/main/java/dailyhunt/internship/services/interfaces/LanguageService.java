package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;

public interface LanguageService {
    Language findLanguageById(Long id);

    List<Language> findAllLanguage();

    Language saveLanguage(NewsComponents language);

    Language updateLanguage(NewsComponents language, Long languageId) throws ResourceNotFoundException;

    Boolean deleteLanguage(Long languageId) throws ResourceNotFoundException;

    List<Language> findAllById(List<Long> ids);

    Language toggleActiveStatus(Long languageId);
}

