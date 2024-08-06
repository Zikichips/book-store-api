package com.example.bookstore_app.auth;

import com.example.bookstore_app.cart.CartService;
import com.example.bookstore_app.dto.LoginRequest;
import com.example.bookstore_app.dto.LoginResponse;
import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserRepository;
import com.example.bookstore_app.user.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CartService cartService;



    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.valueOf("SUBSCRIBER"));
        User newUser = userRepository.save(user);
        if(newUser.getId() != null) {
            cartService.createCart(user.getUsername());
            return new ResponseEntity<>("User created successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User could not be created", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String jwt = jwtUtil.generateToken(loginRequest.getUsername());

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

}

