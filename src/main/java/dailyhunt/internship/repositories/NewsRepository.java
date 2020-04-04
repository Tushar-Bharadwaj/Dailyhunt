package dailyhunt.internship.repositories;

import dailyhunt.internship.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByTitleContaining(String keyword);
}
