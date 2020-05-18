package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.SourceDTO;
import dailyhunt.internship.clientmodels.response.AllSources;
import dailyhunt.internship.entities.Source;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(SourceEndpoints.BASE_URL)
public class SourceEndpoints {

    static final String BASE_URL = "/api/v1/source";

    private final SourceService sourceService;

    @Autowired
    public SourceEndpoints(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AllSources> getAllSources() {
        return ResponseEntity.ok().body(sourceService.getAllSources());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Source> addSource(@Valid @RequestBody SourceDTO sourceDTO) throws IOException {
        Source source = sourceService.save(sourceDTO);
        sourceService.saveSourceAtUserProfile(source);
        return ResponseEntity.ok().body(source);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSource(@Valid @PathVariable Long id) throws ResourceNotFoundException {
        sourceService.delete(id);
        return ResponseEntity.ok().body("source deleted successfully");
    }
}
