package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.clientmodels.request.UpdateNewsRequest;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.entities.User;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.NewsRepository;
import dailyhunt.internship.services.interfaces.*;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/";
    private NewsRepository newsRepository;
    private TagService tagService;
    private GenreService genreService;
    private LocalityService localityService;
    private LanguageService languageService;
    private UserService userService;
    private ImageService imageService;

    public NewsServiceImpl(NewsRepository newsRepository, TagService tagService,
                           GenreService genreService, LocalityService localityService, LanguageService languageService,
                           UserService userService, ImageService imageService) {
        this.newsRepository = newsRepository;
        this.tagService = tagService;
        this.genreService = genreService;
        this.localityService = localityService;
        this.languageService = languageService;
        this.userService = userService;
        this.imageService = imageService;

    }

    @Override
    public News findNewsById(Long id) {
        Optional<News> locality = newsRepository.findById(id);
        if(!locality.isPresent())
            throw new ResourceNotFoundException("This Locality does not exist");
        return locality.get();
    }

    @Override
    public List<News> findAllNews() {
            return newsRepository.findAll();
    }

    @Override
    public void saveNews(NewsRequest newsRequest) throws IOException {

        //Create new tags if not existing
        Set<Tag> tags = new HashSet<>();
        //Check for valid Locality, Language, Genre
        newsRequest.getTags().forEach(currentTag ->
            tags.add(tagService.saveTags(currentTag))
        );


        //Save news
        List<Locality> localities = localityService.findAllById(newsRequest.getLocalityIds());
        List<Language> languages = languageService.findAllById(newsRequest.getLanguageIds());
        List<Genre> genres = genreService.findAllById(newsRequest.getGenreIds());
        User user = userService.findUserById(newsRequest.getUserId());

        if(languages.isEmpty() || genres.isEmpty() || localities.isEmpty()
                || DailyhuntUtil.isNullOrEmpty(user)) {
            throw new BadRequestException("Please Fill All Fields");
        }

        //Updating the post counts of tags, localities, languages and genres
        localities.forEach(locality -> {
            locality.setPostCount(locality.getPostCount() + 1);
            NewsComponents locality_request = NewsComponents.builder()
                    .name(locality.getName())
                    .active(locality.getActive())
                    .postCount(locality.getPostCount())
                    .build();
            localityService.updateLocality(locality_request, locality.getId());
        });
        languages.forEach(language -> {
            language.setPostCount(language.getPostCount() + 1);
            NewsComponents language_request = NewsComponents.builder()
                    .name(language.getName())
                    .active(language.getActive())
                    .postCount(language.getPostCount())
                    .build();
            languageService.updateLanguage(language_request, language.getId());
        });
        genres.forEach(genre -> {
            genre.setPostCount(genre.getPostCount() + 1);
            NewsComponents genre_request = NewsComponents.builder()
                    .name(genre.getName())
                    .active(genre.getActive())
                    .postCount(genre.getPostCount())
                    .build();
            genreService.updateGenre(genre_request, genre.getId());
        });



        News createNews = News.builder()
                .approved(false)
                .createdAt(new Date())
                .draft(false)
                .genres(new HashSet<>(genres))
                .tags(new HashSet<>(tags))
                .localities(new HashSet<>(localities))
                .language(new HashSet<>(languages))
                .published(false)
                .shortText(newsRequest.getShortText())
                .title(newsRequest.getTitle())
                .text(newsRequest.getText())
                .updatedAt(new Date())
                .user(user)
                .trending(false)
                .build();


        News currentNews = newsRepository.save(createNews);
        String filePath = imageService.saveImage(newsRequest.getBase64string());
        currentNews.setImagePath(filePath);
        newsRepository.save(currentNews);
    }

    @Override
    public void updateNews(UpdateNewsRequest updateNewsRequest, Long id) throws
            IOException, ResourceNotFoundException {

        News news = findNewsById(id);

        User user = userService.findUserById(updateNewsRequest.getUserId());

        news.setTitle(updateNewsRequest.getTitle());
        news.setText(updateNewsRequest.getText());
        news.setShortText(updateNewsRequest.getShortText());

        news.setDraft(updateNewsRequest.getDraft());
        if(!DailyhuntUtil.isNullOrEmpty(updateNewsRequest.getPublished()))
            news.setPublished(updateNewsRequest.getPublished());


        Set<Tag> tags = new HashSet<>();
        updateNewsRequest.getTags().forEach(currentTag ->
                tags.add(tagService.saveTags(currentTag))
        );
        List<Locality> localities = localityService.findAllById(updateNewsRequest.getLocalityIds());
        List<Language> languages = languageService.findAllById(updateNewsRequest.getLanguageIds());
        List<Genre> genres = genreService.findAllById(updateNewsRequest.getGenreIds());

        if( languages.isEmpty() || genres.isEmpty() || localities.isEmpty()
                || DailyhuntUtil.isNullOrEmpty(user)) {
            throw new BadRequestException("Please Fill All Fields");
        }

        news.setTags(new HashSet<>(tags));
        news.setLocalities(new HashSet<>(localities));
        news.setLanguage(new HashSet<>(languages));
        news.setGenres(new HashSet<>(genres));

        News updatedNews = newsRepository.save(news);
        String filePath = imageService.saveImage(updateNewsRequest.getBase64string());
        updatedNews.setImagePath(filePath);
        newsRepository.save(updatedNews);

    }

    @Override
    public void deleteNews(Long newsId) throws ResourceNotFoundException {
        newsRepository.deleteById(newsId);
    }


    @Override
    public void setTrending(Long id){
        News currentnews = findNewsById(id);
        currentnews.setTrending(!currentnews.getTrending());
        newsRepository.save(currentnews);
    }

    @Override
    public void publishNews(Long id){
        News currentnews = findNewsById(id);
        currentnews.setPublished(!currentnews.getPublished());
        newsRepository.save(currentnews);
    }

}
