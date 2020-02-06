package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.SignUpForm;
import dailyhunt.internship.entities.Role;
import dailyhunt.internship.entities.User;
import dailyhunt.internship.enums.RoleName;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.RoleRepository;
import dailyhunt.internship.repositories.UserRepository;
import dailyhunt.internship.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserById(Long id) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) {
            throw new ResourceNotFoundException("This user does not exist");
        }
        return user.get();
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public User saveUser(SignUpForm signUpRequest) throws BadRequestException {


        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("This Email Address Is Already Taken");
        }

        // Creating user's account
        User user = User.builder()
                .name(signUpRequest.getName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch(role) {
                case "ADMIN":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new BadRequestException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                case "MODERATOR":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
                            .orElseThrow(() -> new BadRequestException("Fail! -> Cause: User Role not find."));
                    roles.add(pmRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new BadRequestException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    @Override
    public Boolean deleteUser(Long userId) throws ResourceNotFoundException {
        return null;
    }
}
