package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.LanguageIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllGenres;
import dailyhunt.internship.clientmodels.response.AllLanguages;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(LanguageEndpoints.BASE_URL)
public class LanguageEndpoints {
    static final String BASE_URL = "/api/v1/language";
    private final LanguageService languageService;

    @Autowired
    public LanguageEndpoints(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    ResponseEntity<AllLanguages> getAllLanguages() {
        return ResponseEntity.ok().body(languageService.findAllLanguage());
    }

    @PostMapping("/languageIds")
    public ResponseEntity<AllLanguages> getLanguagesByIds(@Valid @RequestBody LanguageIdList languageIdList) {
        return ResponseEntity.ok().body(languageService.findLanguagesByIds(languageIdList));
    }

    @GetMapping("/{languageId}")
    public Language getLanguageById(@PathVariable Long languageId) throws ResourceNotFoundException{
        return languageService.findLanguageById(languageId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Language saveLanguage(@Valid @RequestBody NewsComponents request) {
        Language language = languageService.saveLanguage(request);
        languageService.saveLanguageAtUserProfile(language);
        return language;
    }

    @PutMapping("/name/{languageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Language updateLanguageName(@RequestBody NewsComponents request, @PathVariable Long languageId)
            throws BadRequestException, ResourceNotFoundException {
        return languageService.updateLanguageName(request, languageId);
    }

    @PutMapping("/active/{languageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Language updateLanguageActive(@PathVariable Long languageId) throws
            ResourceNotFoundException {
        return languageService.updateLanguageActive(languageId);

    }

    @DeleteMapping("/{languageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteLanguage(@PathVariable Long languageId) throws ResourceNotFoundException {
        return languageService.deleteLanguage(languageId);
    }
}

