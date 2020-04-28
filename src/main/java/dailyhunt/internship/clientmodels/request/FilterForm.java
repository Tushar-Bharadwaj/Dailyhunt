package dailyhunt.internship.clientmodels.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
public class FilterForm {

    @NotBlank
    private String keyword;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Optional<Date> from_date;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Optional<Date> to_date;

    private Optional<List<String>> tags;

    private Optional<List<Long>> genreIds;

    private Optional<List<Long>> localityIds;
}
