package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.LocalityIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllGenres;
import dailyhunt.internship.clientmodels.response.AllLocalities;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.LocalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public LocalityEndpoints(LocalityService localityService) {
        this.localityService = localityService;
    }

    @GetMapping
    ResponseEntity<AllLocalities> getAllLocalities() {

        return ResponseEntity.ok().body(localityService.findAllLocalities());
    }

    @PostMapping("/localityIds")
    public ResponseEntity<AllLocalities> getLocalitiesByIds(@Valid @RequestBody LocalityIdList localityIdList) {
        return ResponseEntity.ok().body(localityService.findLocalitiesByIds(localityIdList));
    }

    @GetMapping("/{localityId}")
    public Locality getLocalityById(@PathVariable Long localityId) throws ResourceNotFoundException{
        return localityService.findLocalityById(localityId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Locality saveLocality(@Valid @RequestBody NewsComponents request) {
        Locality locality = localityService.saveLocality(request);
        localityService.saveLocalityAtUserProfile(locality);
        return locality;
    }


    @PutMapping("/name/{localityId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Locality updateLocalityName(@Valid @RequestBody NewsComponents request, @PathVariable Long localityId)
            throws BadRequestException, ResourceNotFoundException {
        return localityService.updateLocalityName(request, localityId);
    }

    @PutMapping("/active/{localityId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Locality updateLocalityActive(@PathVariable Long localityId) throws
            ResourceNotFoundException {
        return localityService.updateLocalityActive(localityId);

    }

    @DeleteMapping("/{localityId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteLocality(@PathVariable Long localityId) throws ResourceNotFoundException {
        return localityService.deleteLocality(localityId);
    }
}
