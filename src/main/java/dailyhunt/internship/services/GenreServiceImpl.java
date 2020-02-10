package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.news.components.GenreRepository;
import dailyhunt.internship.services.interfaces.GenreService;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre findGenreById(Long id) throws ResourceNotFoundException{
        Optional<Genre> genre = genreRepository.findById(id);
        if(!genre.isPresent())
            throw new ResourceNotFoundException("This Genre does not exist");
        return genre.get();
    }

    @Override
    public List<Genre> findAllGenre() {
        return genreRepository.findAll();
    }

    @Override
    public Genre saveGenre(NewsComponents genre) {
        if(genreRepository.existsByName(genre.getName()))
            throw new BadRequestException("There already exists a Genre with same name");
        return genreRepository.save(
                Genre.builder()
                        .active(true)
                        .name(StringUtils.capitalize(genre.getName().toLowerCase()))
                        .postCount(0L)
                        .build()
        );
    }

    @Override
    public Genre findGenreByName(String name) throws ResourceNotFoundException {
        Optional<Genre> genre = genreRepository.findByName(name);
        if(!genre.isPresent())
            throw new ResourceNotFoundException("This genre does not exist");
        return genre.get();
    }

    @Override
    public Genre updateGenre(NewsComponents request, Long genreId) throws ResourceNotFoundException {
        Optional<Genre> checkGenre = genreRepository.findById(genreId);
        if(checkGenre.isPresent()) {
            if(DailyhuntUtil.isNullOrEmpty(request.getActive()))
                throw new BadRequestException("Please fill all fields");
            Genre genre = checkGenre.get();
            genre.setActive(request.getActive());
            return genreRepository.save(genre);
        }
        throw new ResourceNotFoundException("Invalid genre selected.");
    }

    @Override
    public Boolean deleteGenre(Long genreId) throws ResourceNotFoundException {
        Optional<Genre> genre = genreRepository.findById(genreId);
        if(genre.isPresent()) {
            genreRepository.deleteById(genreId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid genre selected");
    }

    @Override
    public List<Genre> findAllById(List<Long> ids) {
        return genreRepository.findAllById(ids);
    }
}
