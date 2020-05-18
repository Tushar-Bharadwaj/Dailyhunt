package dailyhunt.internship.clientmodels.response;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SourceRequest {

    @NotNull
    private Long id;

    private String name;

    private String imagepath;
}
