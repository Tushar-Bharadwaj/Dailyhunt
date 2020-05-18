package dailyhunt.internship.clientmodels.response;

import dailyhunt.internship.entities.Source;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllSources {
    private List<Source> all_the_sources;
}
