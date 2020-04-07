package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.LocalityService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(LocalityEndpoints.BASE_URL)
public class LocalityEndpoints {
    static final String BASE_URL = "/api/v1/locality";
    private final LocalityService localityService;


    public LocalityEndpoints(LocalityService localityService) {
        this.localityService = localityService;
    }

    @GetMapping
    List<Locality> getAllLocalities() {
        return localityService.findAllLocalities();
    }

    @GetMapping("/{localityId}")
    public Locality getLocalityById(@PathVariable Long localityId) {
        return localityService.findLocalityById(localityId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Locality saveLocality(@Valid @RequestBody NewsComponents request) {
        return localityService.saveLocality(request);
    }

    @PutMapping("/{localityId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Locality updateLocality(@Valid @RequestBody NewsComponents request, @PathVariable Long localityId) throws ResourceNotFoundException {
        return localityService.updateLocality(request, localityId);
    }

    @DeleteMapping("/{localityId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteLocality(@PathVariable Long localityId) throws ResourceNotFoundException {
        return localityService.deleteLocality(localityId);
    }
}
