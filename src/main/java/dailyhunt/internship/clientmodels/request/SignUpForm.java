package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignUpForm {
    @NotBlank
    @Size(min = 3, max = 50, message = "Name must be atleast 3 to 50 characters long")
    private String name;

    @NotBlank
    @Size(max = 60)
    @Email(message = "Please enter valid email")
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40, message = "Password must be atleast 6 to 40 characters long")
    private String password;
}
