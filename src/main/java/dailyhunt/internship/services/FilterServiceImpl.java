package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.FilterForm;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FilterServiceImpl implements FilterService {

    @Autowired
    private NewsService newsService;

    @Autowired
    private TagService tagService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private LocalityService localityService;

    @Override
    @Transactional
    public List<News> filter(FilterForm filterForm){
        List<News> keyword_filtered = newsService.filterByTitleKeyword(filterForm.getKeyword());

        if(filterForm.getFrom_date() != null){
            for(News news : keyword_filtered){
                if(filterForm.getFrom_date().compareTo(news.getCreatedAt()) > 0)
                    keyword_filtered.remove(news);
            }
        }
        if(filterForm.getTo_date() != null){
            for(News news : keyword_filtered){
                if(filterForm.getTo_date().compareTo(news.getCreatedAt()) < 0)
                    keyword_filtered.remove(news);
            }
        }

        if(filterForm.getTags() != null){
            for(News news : keyword_filtered){
                boolean contains = false;
                for(String tag : filterForm.getTags()) {
                    Optional<Tag> optional = tagService.findTagByName(tag);
                    if (optional.isPresent() && news.getTags().contains(optional.get())) {
                        contains = true;
                        break;
                    }
                }
                if(!contains)
                    keyword_filtered.remove(news);
            }
        }

        if(filterForm.getGenres() != null){
            for(News news : keyword_filtered){
                boolean contains = false;
                for(String genre : filterForm.getGenres()) {
                    Genre optional = genreService.findGenreByName(genre);
                    if (optional != null && news.getTags().contains(optional)) {
                        contains = true;
                        break;
                    }
                }
                if(!contains)
                    keyword_filtered.remove(news);
            }
        }

        if(filterForm.getLocalities() != null){
            for(News news : keyword_filtered){
                boolean contains = false;
                for(String locality : filterForm.getLocalities()) {
                    Optional<Locality> optional = localityService.findLocalityByName(locality);
                    if (optional.isPresent() && news.getTags().contains(optional.get())) {
                        contains = true;
                        break;
                    }
                }
                if(!contains)
                    keyword_filtered.remove(news);
            }
        }

        return keyword_filtered;
    }
}
