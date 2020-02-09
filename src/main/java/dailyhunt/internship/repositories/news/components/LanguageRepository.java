package dailyhunt.internship.repositories.news.components;

import dailyhunt.internship.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByName(String name);
    Boolean existsByName(String name);
}
