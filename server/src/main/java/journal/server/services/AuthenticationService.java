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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public String register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        userService.registerUser(user);

        String jwtToken = jwtService.generateToken(user);

        return Json.createObjectBuilder().add("token", jwtToken).build().toString();
    }

    public String authenticate(User user){

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        Optional<User> opt = userService.findUserByEmail(user.getEmail());
        String jwtToken = jwtService.generateToken(opt.get());

        return Json.createObjectBuilder().add("token", jwtToken).build().toString();
    }
}
