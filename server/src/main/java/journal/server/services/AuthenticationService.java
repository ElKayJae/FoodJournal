package journal.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import journal.server.config.JwtService;
import journal.server.models.AuthenticationRequest;
import journal.server.models.AuthenticationResponse;
import journal.server.models.RegisterRequest;
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
    
    public AuthenticationResponse register(RegisterRequest request){
        User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
        repository.registerUser(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Optional<User> opt = repository.findUserByEmail(request.getEmail());
        System.out.println(request.getEmail());
        String jwtToken = jwtService.generateToken(opt.get());
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }
}
