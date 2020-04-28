package dailyhunt.internship.repositories;

import dailyhunt.internship.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
