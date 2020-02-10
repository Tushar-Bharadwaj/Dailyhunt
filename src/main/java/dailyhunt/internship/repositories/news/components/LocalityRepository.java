package dailyhunt.internship.repositories.news.components;

import dailyhunt.internship.entities.newscomponents.Locality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {
    Optional<Locality> findByName(String name);
    Boolean existsByName(String name);
}
