package dailyhunt.internship.clientmodels.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class FilterForm {

    @NotBlank
    private String keyword;

    @Nullable
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date from_date;

    @Nullable
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date to_date;

    @Nullable
    private List<String> tags;

    @Nullable
    private List<String> genres;

    @Nullable
    private List<String> localities;
}
