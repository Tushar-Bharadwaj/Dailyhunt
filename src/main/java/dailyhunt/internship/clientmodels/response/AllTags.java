package dailyhunt.internship.clientmodels.response;

import dailyhunt.internship.entities.newscomponents.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllTags {
    private List<Tag> all_the_tags;
}
