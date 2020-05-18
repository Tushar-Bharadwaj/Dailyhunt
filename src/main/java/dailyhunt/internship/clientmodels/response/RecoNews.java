package dailyhunt.internship.clientmodels.response;

import dailyhunt.internship.entities.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoNews {

    @NotNull
    private Long id;

    private Source source;

    @NotBlank
    private String title;

    private Set<Long> tagIds;

    private Set<Long> localityIds;

    private Set<Long> genreIds;

    private Set<Long> languageIds;

    private String thumbnailPath;

    private Boolean trending;
}

