package com.example.portalapi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.CredentialsDTO;
import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    Page<UserDTO> getUsers(@PageableDefault(size = 20) Pageable paging) {
        return this.userService.getUsersWithAwards(paging);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<Optional<UserDTO>> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.getUser(id));
    }

    @PostMapping(path = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User user) {
        User u = userService.save(user);
        if (u == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<>(u, HttpStatus.CREATED);
        }
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<User> update(@RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
        User user = userService.update(userDTO);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/users/authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> authenticate(@RequestBody CredentialsDTO credentialsDTO) {
        UserDTO userDTO = userService.authenticate(credentialsDTO);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        }
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                // TODO: 28.11.2021 refactor, move to util
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String user_email = decodedJWT.getSubject();
                User user = userService.getUserByEmail(user_email);
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.getRole().toString())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message x", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("refreshToken is missing");
        }
    }
}
