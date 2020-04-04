package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateNewsRequest {

    @NotNull
    private Long id;

    @NotNull(message = "User ID Required")
    private Long userId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Enter Text For News")
    private String text;

    @NotBlank(message = "Short Text is missing")
    private String shortText;

    @NotEmpty(message = "You must have atleast 1 tag")
    private List<String> tags;

    @NotEmpty(message = "You must enter atleast 1 locality")
    private List<Long> localityIds;

    @NotEmpty(message = "You must have atleast 1 genre")
    private List<Long> genreIds;

    @NotEmpty(message = "You must have atleast 1 language")
    private List<Long> languageIds;

    private List<String> imagePaths;

    private Boolean draft;

    private Boolean published;

    private Boolean approved;

    private String approvedBy;
}
