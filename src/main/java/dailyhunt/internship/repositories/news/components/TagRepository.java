package dailyhunt.internship.repositories.news.components;

import dailyhunt.internship.entities.newscomponents.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    Boolean existsByName(String name);
}
