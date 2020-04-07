package dailyhunt.internship.endpoints;


import dailyhunt.internship.clientmodels.request.SignUpForm;
import dailyhunt.internship.clientmodels.request.UpdateForm;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(UserEndpoints.BASE_URL)
public class UserEndpoints {
    static final String BASE_URL = "/api/v1/user";

    private final UserService userService;

    public UserEndpoints(UserService userService) {
        this.userService = userService;
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

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateForm updateForm){
        userService.updateUser(updateForm);
        return ResponseEntity.ok().body("User details updated successfully");
    }
}
