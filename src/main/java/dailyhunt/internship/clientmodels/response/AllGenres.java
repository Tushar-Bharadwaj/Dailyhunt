package dailyhunt.internship.clientmodels.response;

import dailyhunt.internship.entities.newscomponents.Genre;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllGenres {
    private List<Genre> all_the_genres;
}
