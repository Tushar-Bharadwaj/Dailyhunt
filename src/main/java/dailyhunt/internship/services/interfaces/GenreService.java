package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.GenreIdList;
import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.response.AllGenres;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Optional<Genre> findGenreById(Long id) throws ResourceNotFoundException;

    Optional<Genre> findGenreByName(String name) throws ResourceNotFoundException;

    AllGenres findAllGenre();

    AllGenres findGenresByIds(GenreIdList genreIdList);

    Genre saveGenre(NewsComponents newsComponents);

    void saveGenreAtUserProfile(Genre genre);

    Genre updateGenreName(NewsComponents request, Long genreId)
            throws BadRequestException, ResourceNotFoundException;

    Genre updateGenreActive(Long genreId) throws
            ResourceNotFoundException;

    void incrementGenrePostCount(Genre genre);

    Boolean deleteGenre(Long genreId);

    List<Genre> findAllById(List<Long> ids);

    Genre updateGenreGeneric(Long genreId);
}
