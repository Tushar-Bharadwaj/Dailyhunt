package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import java.util.List;

@Data
public class GenreIdList {
    private List<Long> genreIds;
}
