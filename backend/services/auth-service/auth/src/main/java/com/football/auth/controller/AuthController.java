package com.football.auth.controller;

import com.football.auth.model.User;
import com.football.auth.model.UserRequest;
import com.football.auth.model.UserTeam;
import com.football.auth.repository.UserRepository;
import com.football.auth.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("auths")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KafkaTemplate<String, UserTeam> kafkaTemplate;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, KafkaTemplate<String, UserTeam> kafkaTemplate ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.kafkaTemplate = kafkaTemplate;
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .nameTeam(request.getNameTeam())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamName(request.getNameTeam());
        userTeam.setAccount(request.getEmail());
        userRepository.save(user);
        kafkaTemplate.send("user-team",userTeam);
        return ResponseEntity.ok(User.builder().username(request.getUsername()).nameTeam(request.getNameTeam()).email(request.getEmail()).build());
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        return token;
    }

}
