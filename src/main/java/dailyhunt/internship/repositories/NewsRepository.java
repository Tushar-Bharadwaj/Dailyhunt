package dailyhunt.internship.repositories;

import dailyhunt.internship.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
