package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;

public interface NewsService {
    News findNewsById(Long id);

    List<News> findAllNews();

    News saveNews(NewsRequest news);

    News updateNews(NewsRequest news, Long newsId) throws ResourceNotFoundException;

    Boolean deleteNews(Long newsId) throws ResourceNotFoundException;
}

