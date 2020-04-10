package dailyhunt.internship.clientmodels.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class FilterTags {
    private List<String> tags;
}
