package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.Language;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.LanguageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(LanguageEndpoints.BASE_URL)
public class LanguageEndpoints {
    static final String BASE_URL = "/api/v1/language";
    private final LanguageService languageService;

    public LanguageEndpoints(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    List<Language> getAllLanguages() {
        return languageService.findAllLanguage();
    }

    @GetMapping("/{languageId}")
    public Language getLanguageById(@PathVariable Long languageId) {
        return languageService.findLanguageById(languageId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Language saveLanguage(@Valid @RequestBody NewsComponents request) {
        return languageService.saveLanguage(request);
    }

    @PutMapping("/{languageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Language updateLanguage(@RequestBody NewsComponents request, @PathVariable Long languageId) throws ResourceNotFoundException {
        return languageService.updateLanguage(request, languageId);
    }

    @DeleteMapping("/{languageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteLanguage(@PathVariable Long languageId) throws ResourceNotFoundException {
        return languageService.deleteLanguage(languageId);
    }
}

