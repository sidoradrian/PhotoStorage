package com.nuadu.rest.bootstrap;

import com.nuadu.rest.model.Role;
import com.nuadu.rest.model.User;
import com.nuadu.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * DataLoader creates user and saves it in DB every time when application starts.
 *
 * @author Adrian Sidor
 */
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("Pasword2$s");

        Role role = new Role();
        role.setRoleName("USER");
        user.setRoles(Arrays.asList(role));
        userService.save(user);
    }
}
