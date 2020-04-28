package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import java.util.List;

@Data
public class LocalityIdList {
    private List<Long> localityIds;
}
