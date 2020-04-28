package dailyhunt.internship.clientmodels.response;

import dailyhunt.internship.entities.newscomponents.Language;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllLanguages {
    private List<Language> all_the_languages;
}
