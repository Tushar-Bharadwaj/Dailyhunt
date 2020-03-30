package dailyhunt.internship.clientmodels.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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

    private List<String> genres;

    private List<String> localities;
}
