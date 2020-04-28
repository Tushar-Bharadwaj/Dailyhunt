package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateNewsRequest {

    private String title;

    private String text;

    private String shortText;

    private List<String> tags;

    private List<Long> localityIds;

    private List<Long> genreIds;

    private List<Long> languageIds;

    private String base64string;

}
