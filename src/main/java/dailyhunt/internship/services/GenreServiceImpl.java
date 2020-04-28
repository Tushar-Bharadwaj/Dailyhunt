package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.GenreIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllGenres;
import dailyhunt.internship.clientmodels.response.NewsComponentsRequest;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.news.components.GenreRepository;
import dailyhunt.internship.services.interfaces.GenreService;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository,
                            WebClient.Builder webClientBuilder) {
        this.genreRepository = genreRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Optional<Genre> findGenreById(Long id) throws ResourceNotFoundException{
        Optional<Genre> genre = genreRepository.findById(id);
        if(!genre.isPresent())
            throw new ResourceNotFoundException("This Genre does not exist");
        return genre;
    }

    @Override
    public AllGenres findAllGenre() {

        return AllGenres.builder()
            .all_the_genres(genreRepository.findAll())
            .build();
    }

    @Override
    public AllGenres findGenresByIds(GenreIdList genreIdList) {
        return AllGenres.builder()
                .all_the_genres(genreRepository.findAllById(genreIdList.getGenreIds()))
                .build();
    }

    @Override
    public Genre saveGenre(NewsComponents newsComponents) {
        if(genreRepository.existsByName(newsComponents.getName()))
            throw new BadRequestException("There already exists a Genre with same name");
        return genreRepository.save(
                Genre.builder()
                        .active(true)
                        .name(StringUtils.capitalize(newsComponents.getName().toLowerCase()))
                        .postCount(0L)
                        .build()
        );
    }

    @Override
    public void saveGenreAtUserProfile(Genre genre) {
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        String fooResourceUrl = "http://profile-service/api/v1/user_profile/newsComponents/genre";
        String result = webClientBuilder.build()
                .post()
                .uri(fooResourceUrl)
                .body(Mono.just(newsComponentsRequest), NewsComponentsRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Optional<Genre> findGenreByName(String name) throws ResourceNotFoundException {
        Optional<Genre> genre = genreRepository.findByName(name);
        if(!genre.isPresent())
            throw new ResourceNotFoundException("This genre does not exist");
        return genre;
    }

    @Override
    public Genre updateGenreName(NewsComponents request, Long genreId)
            throws BadRequestException, ResourceNotFoundException {
        if (genreRepository.existsByName(request.getName()))
            throw new BadRequestException("name already taken");
        Optional<Genre> checkGenre = findGenreById(genreId);
        if (checkGenre.isPresent()) {
            Genre genre = checkGenre.get();
            genre.setName(request.getName());
            return genreRepository.save(genre);
        }
        throw new ResourceNotFoundException("Invalid genre");
    }

    @Override
    public Genre updateGenreActive(Long genreId) throws
            ResourceNotFoundException {
        Optional<Genre> checkGenre = findGenreById(genreId);
        if(checkGenre.isPresent()) {
            Genre genre = checkGenre.get();
            genre.setActive(!genre.getActive());
            return genreRepository.save(genre);
        }
        throw new ResourceNotFoundException("Invalid genre selected");
    }

    @Override
    public void incrementGenrePostCount(Genre genre){

        genre.setPostCount(genre.getPostCount() + 1);
    }

    @Override
    public Boolean deleteGenre(Long genreId) throws ResourceNotFoundException {
        Optional<Genre> genre = genreRepository.findById(genreId);
        if(genre.isPresent()) {
            deleteGenreAtUserProfile(genreId);
            genreRepository.deleteById(genreId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid genre selected");
    }

    public void deleteGenreAtUserProfile(Long genreId) {
        Optional<Genre> optional = findGenreById(genreId);
        Genre genre = optional.get();
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        String fooResourceUrl = "http://profile-service/api/v1/user_profile/newsComponents/genre";
        String result = webClientBuilder.build()
                .delete()
                .uri(fooResourceUrl+"/"+genreId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public List<Genre> findAllById(List<Long> ids) {
        return genreRepository.findAllById(ids);
    }
}
