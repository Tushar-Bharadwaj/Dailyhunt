package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NewsService {
    News findNewsById(Long id);

    List<News> findAllNews();

    News saveNews(NewsRequest news, MultipartFile[] files) throws IOException;

    News updateNews(NewsRequest news, Long newsId) throws ResourceNotFoundException;

    Boolean deleteNews(Long newsId) throws ResourceNotFoundException;
}

