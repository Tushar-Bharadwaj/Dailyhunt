package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.FilterForm;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.services.interfaces.FilterService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(FilterEndpoints.BASE_URL)
public class FilterEndpoints {

    static final String BASE_URL = "/api/v1/filter";

    @Autowired
    private FilterService filterService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<News> filterNews(@RequestParam FilterForm filterForm){
        return filterService.filter(filterForm);
    }

}
