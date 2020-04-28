package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.*;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.services.interfaces.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(FilterEndpoints.BASE_URL)
public class FilterEndpoints {

    static final String BASE_URL = "/api/v1/filter";

    private final FilterService filterService;

    @Autowired
    public FilterEndpoints(FilterService filterService) {
        this.filterService = filterService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> filterNews(@Valid @RequestBody FilterForm filterForm){
        return ResponseEntity.ok().body(filterService.filter(filterForm));
    }

    @GetMapping("/keyword")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> filterNewsByKeyword(@Valid @RequestParam String keyword){
        return ResponseEntity.ok().body(filterService.filterByKeyword(keyword));
    }

    @PostMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> filterNewsByDateRange(@Valid @RequestBody DateFilter dateFilter){
        return ResponseEntity.ok().body(filterService.filterByDateRange(dateFilter));
    }

    @PostMapping("/tags")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> filterNewsByTag(@Valid @RequestBody FilterTagIds filterTagIds){
        return ResponseEntity.ok().body(filterService.filterByTag(filterTagIds.getTagIds()));
    }

    @PostMapping("/genreIds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> filterNewsByGenre(@Valid @RequestBody FilterGenreIds filterGenreIds){
        return ResponseEntity.ok().body(filterService.filterByGenre(filterGenreIds.getGenreIds()));
    }

    @PostMapping("/localityIds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> filterNewsByLocality(@Valid @RequestBody FilterLocalityIds filterLocalityIds){
        return ResponseEntity.ok().body(filterService.filterByLocality(filterLocalityIds.getLocalityIds()));
    }

}
