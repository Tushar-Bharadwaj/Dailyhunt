package dailyhunt.internship.clientmodels.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    private Long id;

    private String title;

    private String text;

    private String shortText;

    private String imagePath;

}
