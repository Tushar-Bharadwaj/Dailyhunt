package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.clientmodels.request.UpdateNewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.security.services.UserPrinciple;
import dailyhunt.internship.services.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(NewsEndpoints.BASE_URL)
public class NewsEndpoints {
    static final String BASE_URL = "/api/v1/news";
    private final NewsService newsService;

    @Autowired
    public NewsEndpoints(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    List<News> getAllNews() {
        return newsService.findAllNews();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveNews(@Valid @RequestBody NewsRequest request) throws IOException {
        newsService.saveNews(request);
        return ResponseEntity.ok().body("News Added successfully");

    }

    @PutMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateNews(@RequestBody UpdateNewsRequest updateNewsRequest,
                                             @PathVariable Long newsId)
            throws ResourceNotFoundException, IOException {
    /*    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple user = (UserPrinciple) auth.getPrincipal();
        updateNewsRequest.setUserId(user.getId());
    */    newsService.updateNews(updateNewsRequest, newsId);
        return ResponseEntity.ok().body("News details updated successfully");
    }

    @DeleteMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNews(@Valid @PathVariable Long newsId) throws  ResourceNotFoundException{
        newsService.deleteNews(newsId);
        return ResponseEntity.ok().body("News deleted successfully");
    }

    @PutMapping("/updateTrending/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateTrending(@Valid @PathVariable Long newsId) throws ResourceNotFoundException{
        newsService.updateTrending(newsId);
        return ResponseEntity.ok().body("News trending has been updated");
    }

    @PutMapping("/updatePublished/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updatePublished(@Valid @PathVariable Long newsId) throws ResourceNotFoundException{
        newsService.updatePublished(newsId);
        return ResponseEntity.ok().body("News has been published.");
    }

    @PutMapping("/approve/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateApprove(@Valid @PathVariable Long newsId) throws ResourceNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple user = (UserPrinciple) auth.getPrincipal();
        Long userId = user.getId();
        newsService.updateApprove(userId, newsId);
        return ResponseEntity.ok().body("News has been upproved");
    }
}
