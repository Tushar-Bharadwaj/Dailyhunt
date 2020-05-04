package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsRequest;
import dailyhunt.internship.clientmodels.request.UpdateNewsRequest;
import dailyhunt.internship.clientmodels.response.Article;
import dailyhunt.internship.clientmodels.response.RecoNews;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/";
    private final NewsRepository newsRepository;
    private final TagService tagService;
    private final GenreService genreService;
    private final LocalityService localityService;
    private final LanguageService languageService;
    private final UserService userService;
    private final ImageService imageService;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, TagService tagService,
                           GenreService genreService, LocalityService localityService, LanguageService languageService,
                           UserService userService, ImageService imageService,
                           WebClient.Builder webClientBuilder) {
        this.newsRepository = newsRepository;
        this.tagService = tagService;
        this.genreService = genreService;
        this.localityService = localityService;
        this.languageService = languageService;
        this.userService = userService;
        this.imageService = imageService;

        this.webClientBuilder = webClientBuilder;

    }

    @Override
    public Article findCardNewsById(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if(!optionalNews.isPresent())
            throw new ResourceNotFoundException("This news does not exist");
        News news = optionalNews.get();
        return Article.builder()
                .id(news.getId())
                .title(news.getTitle())
                .shortText(news.getShortText())
                .text(news.getText())
                .imagePath(news.getImagePath())
                .build();
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
        tags.forEach(tagService::incrementTagPostCount);
        localities.forEach(localityService::incrementLocalityPostCount);
        languages.forEach(languageService::incrementLanguagePostCount);
        genres.forEach(genreService::incrementGenrePostCount);



        News createNews = News.builder()
                .approved(false)
                .createdAt(new Date())
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
        Optional<News> optional = newsRepository.findById(id);
        if(!optional.isPresent())
            throw new ResourceNotFoundException("Invalid news");
        News news = optional.get();

        news.setTitle(updateNewsRequest.getTitle() == null
                ? news.getTitle()
                : updateNewsRequest.getTitle());
        news.setText(updateNewsRequest.getText() == null
                ? news.getText()
                : updateNewsRequest.getText());
        news.setShortText(updateNewsRequest.getShortText() == null
                ? news.getShortText()
                : updateNewsRequest.getShortText());

        if(updateNewsRequest.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            updateNewsRequest.getTags().forEach(currentTag ->
                    tags.add(tagService.saveTags(currentTag))
            );
            news.setTags(new HashSet<>(tags));
        }
        List<Locality> localities = localityService.findAllById(updateNewsRequest.getLocalityIds());
        List<Language> languages = languageService.findAllById(updateNewsRequest.getLanguageIds());
        List<Genre> genres = genreService.findAllById(updateNewsRequest.getGenreIds());


        if(localities.size() > 0)
            news.setLocalities(new HashSet<>(localities));
        if(languages.size() > 0)
            news.setLanguage(new HashSet<>(languages));
        if(genres.size() > 0)
            news.setGenres(new HashSet<>(genres));

        news.setUpdatedAt(new Date());
        News updatedNews = newsRepository.save(news);
        String filePath = imageService.saveImage(updateNewsRequest.getBase64string());
        updatedNews.setImagePath(filePath);
        newsRepository.save(updatedNews);

    }

    @Override
    public void updatePublished(Long newsId) throws ResourceNotFoundException{
        Optional<News> news = newsRepository.findById(newsId);
        if(news.isPresent()) {
            News currentNews = news.get();
            if(currentNews.getPublished())
                deleteAtRecoService(currentNews);
            else
                publishAtRecoService(currentNews);
            currentNews.setPublished(!currentNews.getPublished());
            newsRepository.save(currentNews);
        }
        else
            throw new ResourceNotFoundException("invalid news");
    }

    public void publishAtRecoService(News news) {
        RecoNews recoNews = RecoNews.builder()
                .id(news.getId())
                .genreIds(news.getGenres().stream().map(genre -> genre.getId()).collect(Collectors.toSet()))
                .languageIds(news.getLanguage().stream().map(language -> language.getId()).collect(Collectors.toSet()))
                .localityIds(news.getLocalities().stream().map(locality -> locality.getId()).collect(Collectors.toSet()))
                .tagIds(news.getTags().stream().map(tag -> tag.getId()).collect(Collectors.toSet()))
                .thumbnailPath(news.getImagePath().replaceFirst("images", "thumbnails"))
                .title(news.getTitle())
                .trending(news.getTrending())
                .build();

        String recoUrl = "https://dailyhunt-reco-service.herokuapp.com/api/v1/card/publish";
        String result = webClientBuilder.build()
                .post()
                .uri(recoUrl)
                .body(Mono.just(recoNews), RecoNews.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void deleteAtRecoService(News news) {
        String recoUrl = "https://dailyhunt-reco-service.herokuapp.com/api/v1/card/delete";
        String result = webClientBuilder.build()
                .delete()
                .uri(recoUrl+"/"+news.getId())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void deleteNews(Long newsId) throws ResourceNotFoundException {
        News news = newsRepository.findById(newsId).get();
        if(news.getPublished())
            deleteAtRecoService(newsRepository.findById(newsId).get());
        newsRepository.deleteById(newsId);
    }


    @Override
    public void updateTrending(Long id) throws ResourceNotFoundException{
        Optional<News> news = newsRepository.findById(id);
        if(news.isPresent()) {
            News currentNews = news.get();
            currentNews.setTrending(!currentNews.getTrending());
            if(currentNews.getPublished())
                updateTredningAtRecoService(currentNews);
            newsRepository.save(currentNews);
        }
        else
            throw new ResourceNotFoundException("invalid news");
    }

    public void updateTredningAtRecoService(News news) {
        String recoUrl = "https://dailyhunt-reco-service.herokuapp.com/api/v1/card/updateTrending";
        String result = webClientBuilder.build()
                .put()
                .uri(recoUrl+"/"+news.getId())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void updateApprove(Long userId, Long newsId) throws ResourceNotFoundException {
        Optional<News> news = newsRepository.findById(newsId);
        User user = userService.findUserById(userId);
        if(news.isPresent()){
            News currentNews = news.get();
            currentNews.setApproved(!currentNews.getApproved());
            currentNews.setApprovedBy(user);
            newsRepository.save(currentNews);
        }
        else
            throw new ResourceNotFoundException("Invalid news");
    }
}
