package journal.server.services;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import journal.server.config.JwtService;
import journal.server.models.Role;
import journal.server.models.User;
import journal.server.repositories.SQLRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SQLRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public String register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        // User.builder()
        //             .name(user.getName())
        //             .email(user.getEmail())
        //             .password(passwordEncoder.encode(user.getPassword()))
        //             .role(Role.USER)
        //             .build();
        repository.registerUser(user);

        String jwtToken = jwtService.generateToken(user);

        return Json.createObjectBuilder().add("token", jwtToken).build().toString();
    }

    public String authenticate(User user){

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        Optional<User> opt = repository.findUserByEmail(user.getEmail());
        String jwtToken = jwtService.generateToken(opt.get());

        return Json.createObjectBuilder().add("token", jwtToken).build().toString();
    }
}
