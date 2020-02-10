package dailyhunt.internship.repositories.news.components;

import dailyhunt.internship.entities.newscomponents.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
    Boolean existsByName(String name);
}
