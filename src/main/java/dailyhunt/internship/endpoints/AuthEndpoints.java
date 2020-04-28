package dailyhunt.internship.endpoints;

import dailyhunt.internship.clientmodels.request.LoginForm;
import dailyhunt.internship.clientmodels.request.SignUpForm;
import dailyhunt.internship.clientmodels.response.JwtResponse;
import dailyhunt.internship.entities.Role;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.repositories.RoleRepository;
import dailyhunt.internship.security.jwt.JwtProvider;
import dailyhunt.internship.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(AuthEndpoints.BASE_URL)
public class AuthEndpoints {
    static final String BASE_URL = "/api/v1/auth";
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtProvider jwtProvider;

    @Autowired
    public AuthEndpoints(RoleRepository roleRepository, AuthenticationManager authenticationManager, UserService userService, JwtProvider jwtProvider) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) throws BadRequestException {
        userService.saveUser(signUpRequest);
        return ResponseEntity.ok().body("User registered successfully!");
    }

    @GetMapping("/roles")
    public ResponseEntity<String> getRoles() throws BadRequestException {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok().body(roles.toString());
    }
}
