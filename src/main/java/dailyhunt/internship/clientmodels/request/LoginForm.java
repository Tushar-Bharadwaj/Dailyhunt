package dailyhunt.internship.clientmodels.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginForm {
    @NotBlank
    @Size(min=3, max = 60, message = "Email Must Be Atleast 3 to 60 character long.")
    private String email;

    @NotBlank
    @Size(min = 6, max = 40, message = "password must be Atleast 6 to 40 characters long.")
    private String password;
}