package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.TagService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(TagEndpoints.BASE_URL)
public class TagEndpoints {
    static final String BASE_URL = "/api/v1/tag";
    private final TagService tagService;


    public TagEndpoints(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    List<Tag> getAllTags() {
        return tagService.findAllTags();
    }

    @GetMapping("/{tagId}")
    public Tag getTagById(@PathVariable Long tagId) {
        return tagService.findTagsById(tagId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag saveTag(@Valid @RequestBody NewsComponents request) {
        return tagService.saveTags(request);
    }

    @PutMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Tag updateTag(@Valid @RequestBody NewsComponents request, @PathVariable Long tagId) throws ResourceNotFoundException {
        return tagService.updateTags(request, tagId);
    }

    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteTag(@PathVariable Long tagId) throws ResourceNotFoundException {
        return tagService.deleteTags(tagId);
    }
}

