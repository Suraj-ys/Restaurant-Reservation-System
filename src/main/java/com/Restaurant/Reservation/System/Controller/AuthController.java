package com.Restaurant.Reservation.System.Controller;


import com.Restaurant.Reservation.System.Entity.Role;
import com.Restaurant.Reservation.System.Entity.User;
import com.Restaurant.Reservation.System.Payload.LoginDTO;
import com.Restaurant.Reservation.System.Payload.SignUpDTO;
import com.Restaurant.Reservation.System.Repository.RoleRepository;
import com.Restaurant.Reservation.System.Repository.UserRepository;
import com.Restaurant.Reservation.System.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Collections;


@RestController
@RequestMapping("/files/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    //http://localhost:8080/files/auth/sign-in
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("User signed in successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    //http://localhost:8080/files/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        User user = new User(
                signUpDTO.getName(),
                signUpDTO.getUsername(),
                signUpDTO.getEmail(),
                passwordEncoder.encode(signUpDTO.getPassword())
        );

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        // Send registration confirmation email
        String emailBody = "Dear Customer " + signUpDTO.getName() + ",\n\n"
                + "Your account has been successfully registered\n"
                + "Username: " + signUpDTO.getUsername() + "\n"
                + "Password: " + signUpDTO.getPassword() + "\n\n"
                + "keep your credentials secure.";

        emailService.sendSimpleEmail(signUpDTO.getEmail(), "Account Registration", emailBody);

        return ResponseEntity.ok("User registered successfully");
    }

}