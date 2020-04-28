package dailyhunt.internship.clientmodels.response;

import dailyhunt.internship.entities.newscomponents.Locality;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllLocalities {

    private List<Locality> all_the_localities;
}
