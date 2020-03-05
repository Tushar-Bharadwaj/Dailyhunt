package dailyhunt.internship.endpoints;


import dailyhunt.internship.clientmodels.request.SignUpForm;
import dailyhunt.internship.clientmodels.response.UserResponse;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.security.services.UserPrinciple;
import dailyhunt.internship.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(UserEndpoints.BASE_URL)
public class UserEndpoints {
    static final String BASE_URL = "/api/v1/user";

    private final UserService userService;

    public UserEndpoints(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@Valid @RequestBody SignUpForm signUpRequest) throws BadRequestException {

        userService.saveUser(signUpRequest);
        return ResponseEntity.ok().body("User created successfully!");
    }

    @DeleteMapping("{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("User deleted successfuly");
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(userService.findUserById(userId));
    }

    @GetMapping("/info")
    public ResponseEntity<?> user() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple user = (UserPrinciple) auth.getPrincipal();
        return ResponseEntity.ok().body(UserResponse.from(userService.findUserById(user.getId())));
    }



}
