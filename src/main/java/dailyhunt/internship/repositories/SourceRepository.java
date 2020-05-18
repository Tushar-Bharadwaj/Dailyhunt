package dailyhunt.internship.repositories;

import dailyhunt.internship.entities.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source, Long> {

    Boolean existsByName(String name);

}
