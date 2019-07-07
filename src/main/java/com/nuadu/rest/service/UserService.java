package com.nuadu.rest.service;

import com.nuadu.rest.exception.UserNotFoundException;
import com.nuadu.rest.model.Role;
import com.nuadu.rest.model.User;
import com.nuadu.rest.model.UserDto;
import com.nuadu.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves a user by its id.
     *
     * @param userId
     * @return the user with given id
     * @throws UserNotFoundException if user not found.
     */
    public User findById(String userId) {
        Optional<User> user = userRepository.findById(Long.parseLong(userId));

        if(!user.isPresent()) {
            throw new UserNotFoundException("User not found. Affected user id: " + userId.toString());
        }

        return user.get();
    }

    public User save(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        Role role = new Role();
        role.setRoleName("USER");
        user.setRoles(Arrays.asList(role));

        return save(user);
    }

    public User save(User user){
        boolean emailExists = userRepository.findByEmail(user.getEmail()) != null;

        if (!emailExists) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Email: " + user.getEmail() + " exists.");
        }
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
