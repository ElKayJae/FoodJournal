package journal.server.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import journal.server.models.User;
import journal.server.services.AuthenticationService;
import journal.server.services.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody User request) {
        Optional<User> opt = userService.findUserByEmail(request.getEmail());
        if (opt.isPresent()) {
            String message = request.getEmail() + " is already taken";
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("message", message).build().toString());
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @RequestBody User request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
