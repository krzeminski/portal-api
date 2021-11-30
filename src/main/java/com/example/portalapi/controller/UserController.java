package com.example.portalapi.controller;

import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.exception.EmailNotFoundException;
import com.example.portalapi.model.HttpResponse;
import com.example.portalapi.service.UserService;
import com.example.portalapi.utility.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.portalapi.constant.EmailConstant.EMAIL_SENT;
import static com.example.portalapi.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/me")
    @PreAuthorize("#id == authentication.principal.id")
    ResponseEntity<Optional<UserDTO>> getUser(Long id) {
        return ResponseEntity.ok(this.userService.getUserDTO(id));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    Page<UserDTO> getUsers(@PageableDefault(size = 20) Pageable paging) {
        return this.userService.getUsers(paging);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.userService.getUserDTO(id));
    }

    @PutMapping(path = "/users/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<User> update(@RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
        User user = userService.update(userDTO);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reset/password/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws MessagingException, EmailNotFoundException {
        userService.resetPassword(email);
        return new ResponseEntity<>(new HttpResponse(OK.value(), OK, OK.getReasonPhrase().toUpperCase(),
                EMAIL_SENT + email), OK);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: 29.11.2021 refresh token
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
            throw new RuntimeException("refreshToken is missing");
        }
        String refresh_token = authorizationHeader.substring(TOKEN_PREFIX.length());
        String user_email = jwtTokenProvider.getSubject(refresh_token);
        if (jwtTokenProvider.isTokenValid(user_email, refresh_token)) {
            User user = userService.getUserByEmail(user_email);
            String access_token = jwtTokenProvider.generateJwtToken(user);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } else {
            SecurityContextHolder.clearContext();
        }
//
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(refresh_token);
//                String user_email = decodedJWT.getSubject();
//                User user = userService.getUserByEmail(user_email);
//                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//                String access_token = JWT.create()
//                        .withSubject(user.getEmail())
//                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
//                        .withIssuer(request.getRequestURI())
//                        .withClaim("roles", user.getRole().toString())
//                        .sign(algorithm);
//
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refresh_token);
//
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//            } catch (Exception exception) {
//                response.setHeader("error", exception.getMessage());
//                response.setStatus(FORBIDDEN.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message x", exception.getMessage());
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//        } else {
//            throw new RuntimeException("refreshToken is missing");
//        }
    }
}
