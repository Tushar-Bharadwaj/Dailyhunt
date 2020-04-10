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

    News saveNews(NewsRequest news) throws IOException;

//    News updateNews(NewsRequest news, Long newsId) throws ResourceNotFoundException;

    void deleteNews(Long newsId) throws ResourceNotFoundException;

    News updateNews(UpdateNewsRequest updateNewsRequest);


    void setTrending(List<Long> ids);

    void resetTrending(List<Long> ids);
}

