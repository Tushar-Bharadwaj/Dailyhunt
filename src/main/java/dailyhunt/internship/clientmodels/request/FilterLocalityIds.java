package dailyhunt.internship.clientmodels.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FilterLocalityIds {
    private List<Long> localityIds;
}
