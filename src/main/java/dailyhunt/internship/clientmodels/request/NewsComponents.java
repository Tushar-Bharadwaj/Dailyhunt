package dailyhunt.internship.clientmodels.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewsComponents {
    @NotBlank
    @Size(min = 3, max = 50, message = "Name must me 3 to 50 characters long")
    private String name;
    private Boolean active;
    private Long postCount;
}
