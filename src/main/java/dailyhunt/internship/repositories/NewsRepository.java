package dailyhunt.internship.repositories;

import dailyhunt.internship.entities.News;
import dailyhunt.internship.entities.newscomponents.Genre;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.entities.newscomponents.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

 //   List<News> findByTitleContaining(String keyword);


}
