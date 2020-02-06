package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.SignUpForm;
import dailyhunt.internship.entities.User;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findUserById(Long id) throws ResourceNotFoundException;

    List<User> findAllUsers();

    User saveUser(SignUpForm user);

    Boolean deleteUser(Long userId) throws ResourceNotFoundException;

}
