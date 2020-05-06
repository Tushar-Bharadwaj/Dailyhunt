package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.GenreIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllGenres;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(GenreEndpoints.BASE_URL)
public class GenreEndpoints {
    static final String BASE_URL = "/api/v1/genre";
    private final GenreService genreService;

    @Autowired
    public GenreEndpoints(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    ResponseEntity<AllGenres> getAllGenres() {

        return ResponseEntity.ok().body(genreService.findAllGenre());
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable Long genreId) throws ResourceNotFoundException{

        return genreService.findGenreById(genreId).get();
    }

    @PostMapping("/genreIds")
    public ResponseEntity<AllGenres> getGenresByIds(@Valid @RequestBody GenreIdList genreIdList) {
        return ResponseEntity.ok().body(genreService.findGenresByIds(genreIdList));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Genre saveGenre(@Valid @RequestBody NewsComponents request) {

        Genre genre = genreService.saveGenre(request);
        genreService.saveGenreAtUserProfile(genre);
        return genre;
    }

    @PutMapping("/name/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Genre updateGenreName(@RequestBody NewsComponents request, @PathVariable Long genreId)
            throws BadRequestException, ResourceNotFoundException {
        return genreService.updateGenreName(request, genreId);
    }

    @PutMapping("/active/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Genre updateGenreActive(@PathVariable Long genreId) throws
            ResourceNotFoundException {
        return genreService.updateGenreActive(genreId);
    }

    @DeleteMapping("/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteGenre(@PathVariable Long genreId) throws ResourceNotFoundException {
        return genreService.deleteGenre(genreId);

    }

    @PutMapping("/generic/{genreId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateGenreGeneric(@PathVariable Long genreId) throws ResourceNotFoundException {
        genreService.updateGenreGeneric(genreId);
        return ResponseEntity.ok().body("genre generic updated");
    }
}
