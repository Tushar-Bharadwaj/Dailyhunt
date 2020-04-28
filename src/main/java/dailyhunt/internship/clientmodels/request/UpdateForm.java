package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50, message = "Name must be atleast 3 to 50 characters long")
    private String name;

    @NotBlank
    @Size(max = 60)
    @Email(message = "Please enter valid email")
    private String email;

    private Set<String> role;
}

