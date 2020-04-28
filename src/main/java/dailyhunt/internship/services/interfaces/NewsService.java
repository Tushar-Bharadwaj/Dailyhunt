package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.clientmodels.request.UpdateNewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface NewsService {
    News findNewsById(Long id);

    List<News> findAllNews();

    void saveNews(NewsRequest news) throws IOException;

//    News updateNews(NewsRequest news, Long newsId) throws ResourceNotFoundException;

    void deleteNews(Long newsId) throws ResourceNotFoundException;

    void updateNews(UpdateNewsRequest updateNewsRequest, Long newsId) throws
            ResourceNotFoundException, IOException;


    void updateTrending(Long id) throws ResourceNotFoundException;
    void updatePublished(Long id) throws ResourceNotFoundException;

    void updateApprove(Long userId, Long newsId) throws ResourceNotFoundException;
}

