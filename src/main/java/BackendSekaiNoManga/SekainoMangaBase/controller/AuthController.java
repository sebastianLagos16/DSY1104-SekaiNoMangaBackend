package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.config.JwtService;
import BackendSekaiNoManga.SekainoMangaBase.model.Role;
import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.ChangePasswordDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.RegisterDTO;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import BackendSekaiNoManga.SekainoMangaBase.service.AuthService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserRepository users;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @PostMapping("/register")
  public User register(@Valid @RequestBody RegisterDTO dto) {
    return authService.register(dto);
  }

  @PostMapping("/change-password")
  public void changePassword(Authentication auth,
      @Valid @RequestBody ChangePasswordDTO dto) {
    authService.changePassword(auth, dto);
  }

  // Login JWT
  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest req) {
    User u = users.findByEmail(req.getEmail())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

    if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
    }

    UserDetails userDetails = org.springframework.security.core.userdetails.User
        .withUsername(u.getEmail())

        .password(u.getPasswordHash())
        .authorities(u.getRoles().stream().map(Role::getName).toArray(String[]::new))
        .build();

    String token = jwtService.generateToken(userDetails);

    return new LoginResponse(
        token,
        u.getId(),
        u.getEmail(),
        u.getNombre(),
        u.getRoles().stream().map(Role::getName).toList());
  }

  // usando el Authentication seteado por el filtro JWT
  @GetMapping("/me")
  public MeDTO me(Authentication auth) {
    if (auth == null || auth.getName() == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
    }

    User u = users.findByEmail(auth.getName())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    MeDTO dto = new MeDTO();
    dto.setId(u.getId());
    dto.setEmail(u.getEmail());
    dto.setNombre(u.getNombre());
    dto.setRoles(u.getRoles().stream().map(Role::getName).toList());
    return dto;
  }

  // dtos simples
  @Data
  public static class LoginRequest {
    private String email;
    private String password;
  }

  public record LoginResponse(
      String token,
      Long id,
      String email,
      String nombre,
      List<String> roles) {
  }

  @Data
  public static class MeDTO {
    private Long id;
    private String email;
    private String nombre;
    private List<String> roles;
  }
}
