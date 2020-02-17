package dailyhunt.internship.services;

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
import dailyhunt.internship.services.interfaces.*;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public News saveNews(NewsRequest news, MultipartFile[] files) throws IOException {

        //Create new tags if not existing
        Set<Tag> tags = new HashSet<>();
        //Check for valid Locality, Language, Genre
        news.getTags().forEach(currentTag ->
            tags.add(tagService.saveTags(currentTag))
        );
        //Save news
        List<Locality> localities = localityService.findAllById(news.getLocalityIds());
        List<Language> languages = languageService.findAllById(news.getLanguageIds());
        List<Genre> genres = genreService.findAllById(news.getGenreIds());
        User user = userService.findUserById(news.getUserId());

        if(languages.isEmpty() || genres.isEmpty() || localities.isEmpty()
                || DailyhuntUtil.isNullOrEmpty(user)) {
            throw new BadRequestException("Please Fill All Fields");
        }



        News createNews = News.builder()
                .approved(false)
                .createdAt(new Date())
                .draft(false)
                .genres(new HashSet<>(genres))
                .tags(new HashSet<>(tags))
                .localities(new HashSet<>(localities))
                .language(new HashSet<>(languages))
                .published(false)
                .shortText(news.getShortText())
                .title(news.getTitle())
                .text(news.getText())
                .updatedAt(new Date())
                .user(user)
                .build();


        News currentNews = newsRepository.save(createNews);
        Set<Image> images = new HashSet<>();
        int count = 0;
        for(MultipartFile file : files) {
            String filePath = imageService.saveImage(file, news.getImageMD5s().get(count++));
            //TODO : Attach the file paths to News
        }
        return currentNews;


    }

    @Override
    public News updateNews(NewsRequest news, Long newsId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public Boolean deleteNews(Long newsId) throws ResourceNotFoundException {
        return null;
    }
}
