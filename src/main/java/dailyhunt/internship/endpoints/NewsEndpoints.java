package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.clientmodels.request.UpdateNewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.services.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(NewsEndpoints.BASE_URL)
public class NewsEndpoints {
    static final String BASE_URL = "/api/v1/news";
    @Autowired
    private NewsService newsService;

    @GetMapping
    List<News> getAllNews() {
        return newsService.findAllNews();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public News saveNews(@Valid @RequestBody NewsRequest request) throws IOException {
        return newsService.saveNews(request);
    }

    @PutMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateNews(@RequestBody UpdateNewsRequest updateNewsRequest,
                                             @PathVariable Long newsId)
            throws ResourceNotFoundException, IOException {
        newsService.updateNews(updateNewsRequest, newsId);
        return ResponseEntity.ok().body("News details updated successfully");
    }

    @DeleteMapping("/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNews(@Valid @PathVariable Long newsId){
        newsService.deleteNews(newsId);
        return ResponseEntity.ok().body("News deleted successfully");
    }

    @PostMapping("/set_trending/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> setTrending(@Valid @PathVariable Long newsId){
        newsService.setTrending(newsId);
        return ResponseEntity.ok().body("news set to trending");
    }

    @PostMapping("/reset_trending/{newsId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetTrending(@Valid @PathVariable Long newsId){
        newsService.resetTrending(newsId);
        return ResponseEntity.ok().body("news reset to trending");
    }
}
