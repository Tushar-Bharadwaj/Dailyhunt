package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(GenreEndpoints.BASE_URL)
public class GenreEndpoints {
    static final String BASE_URL = "/api/v1/genre";
    private final GenreService genreService;

    public GenreEndpoints(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    List<Genre> getAllGenres() {
        return genreService.findAllGenre();
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable Long genreId) {
        return genreService.findGenreById(genreId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Genre saveGenre(@Valid @RequestBody NewsComponents request) {
        return genreService.saveGenre(request);
    }

    @PutMapping("/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Genre updateGenre(@RequestBody NewsComponents request, @PathVariable Long genreId) throws ResourceNotFoundException {
        return genreService.updateGenre(request, genreId);
    }

    @DeleteMapping("/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteGenre(@PathVariable Long genreId) throws ResourceNotFoundException {
        return genreService.deleteGenre(genreId);
    }

    @PutMapping("/activity/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Genre activity(@PathVariable Long genreId) throws ResourceNotFoundException {
        return genreService.toggleActiveStatus(genreId);
    }
}
