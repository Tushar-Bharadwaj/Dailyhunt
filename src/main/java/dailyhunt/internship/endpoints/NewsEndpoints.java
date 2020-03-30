package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.services.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
    public News saveNews(@Valid @RequestBody NewsRequest request, @RequestParam("uploadingFiles") MultipartFile[] uploadingFiles) throws IOException {
        return newsService.saveNews(request, uploadingFiles);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateNews(@Valid @RequestBody NewsRequest newsRequest){
        newsService.updateNews(newsRequest);
        return ResponseEntity.ok().body("News details updated successfully");
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNews(@Valid @RequestBody Long newsid){
        newsService.deleteNews(newsid);
        return ResponseEntity.ok().body("News deleted successfully");
    }

    @PostMapping("/set_trending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> setTrending(@Valid @RequestBody List<Long> ids){
        newsService.setTrending(ids);
        return ResponseEntity.ok().body("news set to trending");
    }

    @PostMapping("/reset_trending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetTrending(@Valid @RequestBody List<Long> ids){
        newsService.resetTrending(ids);
        return ResponseEntity.ok().body("news reset to trending");
    }
}
