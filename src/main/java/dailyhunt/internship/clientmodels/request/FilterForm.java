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

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date from_date;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date to_date;

    private List<String> tags;

    private List<Long> genreIds;

    private List<Long> localityIds;
}
