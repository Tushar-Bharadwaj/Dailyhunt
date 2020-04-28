package dailyhunt.internship.endpoints.newscomponents;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.request.TagIdList;
import dailyhunt.internship.clientmodels.response.AllGenres;
import dailyhunt.internship.clientmodels.response.AllTags;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public TagEndpoints(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    ResponseEntity<AllTags> getAllTags() {
        return ResponseEntity.ok().body(tagService.findAllTags());
    }

    @GetMapping("/{tagId}")
    public Tag getTagById(@PathVariable Long tagId) {
        return tagService.findTagsById(tagId);
    }

    @PostMapping("/tagIds")
    public ResponseEntity<AllTags> getTagsByIds(@Valid @RequestBody TagIdList tagIdList) {
        return ResponseEntity.ok().body(tagService.findTagsByIds(tagIdList));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag saveTag(@Valid @RequestBody NewsComponents request) {
        Tag tag = tagService.saveTags(request);
        tagService.saveTagAtUserProfile(tag);
        return tag;
    }

    @PutMapping("/name/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Tag updateTagName(@Valid @RequestBody NewsComponents request, @PathVariable Long tagId)
            throws BadRequestException, ResourceNotFoundException {
        return tagService.updateTagName(request, tagId);
    }

    @PutMapping("/active/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Tag updateTagActive(@PathVariable Long tagId) throws
            ResourceNotFoundException {
        return tagService.updateTagActive(tagId);

    }

    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteTag(@PathVariable Long tagId) throws ResourceNotFoundException {
        return tagService.deleteTags(tagId);
    }
}

