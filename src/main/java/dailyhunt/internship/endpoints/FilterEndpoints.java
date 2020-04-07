package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.FilterForm;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.services.interfaces.FilterService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(FilterEndpoints.BASE_URL)
public class FilterEndpoints {

    static final String BASE_URL = "/api/v1/filter";

    @Autowired
    private FilterService filterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> filterNews(@RequestBody FilterForm filterForm){
        return ResponseEntity.ok().body(filterService.filter(filterForm));
    }

}
