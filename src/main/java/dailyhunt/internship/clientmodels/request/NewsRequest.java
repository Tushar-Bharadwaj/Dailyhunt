package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewsRequest {
    @NotBlank(message = "User ID Required")
    private Long userId;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Enter Text For News")
    private String text;
    @NotBlank(message = "Short Text is missing")
    private String shortText;

    @NotBlank(message = "You must have atleast 1 tag")
    private List<String> tags;
    @NotBlank(message = "You must enter atleast 1 locality")
    private List<Long> localityIds;

    @NotBlank(message = "You must have atleast 1 genre")
    private List<Long> genreIds;

    @NotBlank(message = "You must have atleast 1 language")
    private List<Long> languageIds;

    private List<String> imageMD5s;

    private Boolean draft;

    private Boolean published;

    private Boolean approved;

    private Long approvedBy;
}
