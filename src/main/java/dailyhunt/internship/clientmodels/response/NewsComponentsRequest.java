package dailyhunt.internship.clientmodels.response;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewsComponentsRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
