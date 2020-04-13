package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.DateFilter;
import dailyhunt.internship.clientmodels.request.FilterForm;
import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.repositories.NewsRepository;
import dailyhunt.internship.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    private NewsRepository newsRepository;

    /*    @Override
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

            if(filterForm.getTags().size() > 0){
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

            if(filterForm.getGenres().size() > 0){
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

            if(filterForm.getLocalities().size() > 0){
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

     */
    @Override
    public List<News> filter(FilterForm filterForm) {
        List<News> keyword_filtered = filterByKeyword(filterForm.getKeyword());

        if (keyword_filtered.size() > 0 && filterForm.getFrom_date().isPresent()) {
            keyword_filtered.removeIf(news -> filterForm.getFrom_date().get().after(news.getCreatedAt()));
        }
        if (filterForm.getTo_date() != null) {
            keyword_filtered.removeIf(news -> filterForm.getTo_date().get().before(news.getCreatedAt()));
        }

        if (keyword_filtered.size() > 0 && filterForm.getTags().isPresent()) {
            List<Tag> tags_list = new ArrayList<Tag>();
            for(String name : filterForm.getTags().get()) {
                Optional optional = tagService.findTagByName(name);
                if(optional.isPresent())
                    tags_list.add((Tag)optional.get());
            }
            if(tags_list.size() == 0)
                throw new BadRequestException("invalid tag");
            keyword_filtered.removeIf(news -> !containsAnyTag(news.getTags(), tags_list));
        }

        if (keyword_filtered.size() > 0 && filterForm.getGenreIds().isPresent()) {

            keyword_filtered.removeIf(news -> !containsAnyGenre(news.getGenres(),
                    genreService.findAllById(filterForm.getGenreIds().get())));
        }

        if (keyword_filtered.size() > 0 && filterForm.getLocalityIds().isPresent()) {

            keyword_filtered.removeIf(news -> !containsAnyLocality(news.getLocalities(),
                    localityService.findAllById(filterForm.getLocalityIds().get())));

        }

        return keyword_filtered;
    }

    @Override
    public List<News> filterByKeyword(String keyword) {
        return newsRepository.findAllByTitleContaining(keyword);
    }

    @Override
    public List<News> filterByDateRange(DateFilter dateFilter) {
        return newsRepository.findByCreatedAtBetween(dateFilter.getFrom_date(), dateFilter.getTo_date());
    }

    @Override
    public List<News> filterByTag(List<String> tags) {
        List<Tag> tags_list = new ArrayList<Tag>();
        for(String name : tags) {
            Optional optional = tagService.findTagByName(name);
            if(optional.isPresent())
                tags_list.add((Tag)optional.get());
        }
        if(tags_list.size() == 0)
            throw new BadRequestException("invalid tag");
        return newsRepository.findByTagsIn(tags_list);
    }

    @Override
    public List<News> filterByGenre(List<Long> genreIds) {
        return newsRepository.findByGenresIn(genreService.findAllById(genreIds));
    }

    @Override
    public List<News> filterByLocality(List<Long> localityIds){
        return newsRepository.findByLocalitiesIn(localityService.findAllById(localityIds));
    }


    public boolean containsAnyTag(Set<Tag> first, List<Tag> second){
        for(Tag tag : second){
            if(first.contains(tag)){
                return true;
            }
        }
        return false;
    }

    public boolean containsAnyGenre(Set<Genre> first, List<Genre> second){
        for(Genre genre : second){
            if(first.contains(genre)){
                return true;
            }
        }
        return false;
    }

    public boolean containsAnyLocality(Set<Locality> first, List<Locality> second){
        for(Locality locality : second){
            if(first.contains(locality)){
                return true;
            }
        }
        return false;
    }
}
