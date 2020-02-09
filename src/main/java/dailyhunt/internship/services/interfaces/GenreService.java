package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.Genre;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre findGenreById(Long id) throws ResourceNotFoundException;

    Genre findGenreByName(String name) throws ResourceNotFoundException;

    List<Genre> findAllGenre();

    Genre saveGenre(NewsComponents newsComponents);

    Genre updateGenre(NewsComponents newsComponents, Long genreId) throws ResourceNotFoundException;

    Boolean deleteGenre(Long genreId);

    List<Genre> findAllById(List<Long> ids);
}
