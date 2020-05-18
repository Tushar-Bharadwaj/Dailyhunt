package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SourceDTO {

    @NotBlank(message = "source name is required")
    private String name;

    private String base64;
}
