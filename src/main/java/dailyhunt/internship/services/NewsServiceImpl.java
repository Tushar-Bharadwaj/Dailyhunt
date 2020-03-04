package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.entities.Image;
import dailyhunt.internship.entities.News;
import dailyhunt.internship.entities.User;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.entities.newscomponents.Language;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.NewsRepository;
import dailyhunt.internship.repositories.UserRepository;
import dailyhunt.internship.repositories.news.components.GenreRepository;
import dailyhunt.internship.repositories.news.components.LanguageRepository;
import dailyhunt.internship.repositories.news.components.LocalityRepository;
import dailyhunt.internship.repositories.news.components.TagRepository;
import dailyhunt.internship.services.interfaces.*;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
    public News saveNews(NewsRequest newsRequest, MultipartFile[] files) throws IOException {

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
                .build();


        News currentNews = newsRepository.save(createNews);
        Set<Image> images = new HashSet<>();
        int count = 0;
        for(MultipartFile file : files) {
            String filePath = imageService.saveImage(file, newsRequest.getImagePaths().get(count++));
            //TODO : Attach the file paths to News
            images.add(Image.builder()
                    .news(currentNews)
                    .path(filePath)
                    .build());
        }

        return currentNews;


    }

    @Override
    public News updateNews(NewsRequest newsRequest) throws ResourceNotFoundException {
        Optional<News> optional = newsRepository.findById(newsRequest.getId());
        if(!optional.isPresent())
            throw new ResourceNotFoundException("Invalid news");
        News news = optional.get();
    //    news.setId(newsRequest.getId());

        User user = userService.findUserById(newsRequest.getUserId());

        news.setUser(user);

        news.setTitle(newsRequest.getTitle());
        news.setText(newsRequest.getText());
        news.setShortText(newsRequest.getShortText());

        news.setDraft(newsRequest.getDraft());
        news.setPublished(newsRequest.getPublished());
        news.setApproved(newsRequest.getApproved());

        User approver = userService.findUserByName(newsRequest.getApprovedBy());
        news.setApprovedBy(approver);

        Set<Tag> tags = new HashSet<>();
        newsRequest.getTags().forEach(currentTag ->
                tags.add(tagService.saveTags(currentTag))
        );
        List<Locality> localities = localityService.findAllById(newsRequest.getLocalityIds());
        List<Language> languages = languageService.findAllById(newsRequest.getLanguageIds());
        List<Genre> genres = genreService.findAllById(newsRequest.getGenreIds());

        if( languages.isEmpty() || genres.isEmpty() || localities.isEmpty()
                || DailyhuntUtil.isNullOrEmpty(user)) {
            throw new BadRequestException("Please Fill All Fields");
        }

        news.setTags(new HashSet<>(tags));
        news.setLocalities(new HashSet<>(localities));
        news.setLanguage(new HashSet<>(languages));
        news.setGenres(new HashSet<>(genres));

        News currentNews = newsRepository.save(news);

        Set<Image> images = new HashSet<>();
        for(String path : newsRequest.getImagePaths()){
            Image.builder()
                    .path(path)
                    .news(currentNews)
                    .build();
        }

        return currentNews;
    }

    @Override
    public void deleteNews(Long newsId) throws ResourceNotFoundException {
        newsRepository.deleteById(newsId);
    }
}
