package security.controllers;

import org.springframework.web.bind.annotation.*;
import security.auth.AuthenticationRequest;
import security.auth.AuthenticationResponse;
import security.auth.RegisterRequest;
import security.auth.services.AuthenticationService;
import security.services.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response
    ) {
        var authResponse = authenticationService.authenticate(authenticationRequest);
        var token = authResponse.getToken();
        response.addCookie(createCookie(token));
        return ResponseEntity.ok(LoginResponse.builder().token(token).build());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        if(userService.getUserByEmail(registerRequest.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(null);
        }

        var res = authenticationService.registerUser(registerRequest);

        response.addCookie(createCookie(res.getToken()));
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/data")
    public ResponseEntity<List<String>> getData() {
        return ResponseEntity.ok().body(userService.getAllUsernames());
    }

    @GetMapping("/api/data/size")
    public ResponseEntity<Integer> getDataSize() {
        return ResponseEntity.ok().body(userService.getAllUsernames().size());
    }

    private Cookie createCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }
}
