package BackendSekaiNoManga.SekainoMangaBase.service;

import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.model.Role;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.RegisterDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.ChangePasswordDTO;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import BackendSekaiNoManga.SekainoMangaBase.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository users;
  private final RoleRepository roles;
  private final PasswordEncoder encoder;

  @Transactional
  public User register(RegisterDTO dto) {
    if (!dto.passwordsMatch()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden.");
    }
    if (users.existsByEmail(dto.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado.");
    }

    User u = new User();
    u.setEmail(dto.getEmail().trim().toLowerCase());
    u.setNombre(dto.getNombre());
    u.setEnabled(true);
    u.setPasswordHash(encoder.encode(dto.getPassword()));

    // Asignar rol USER por defecto
    Role rUser = roles.findByName("ROLE_USER")
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, "Falta ROLE_USER"));
    u.getRoles().add(rUser);

    return users.save(u); // guardar con rol incluido
  }

  @Transactional
  public void changePassword(Authentication auth, ChangePasswordDTO dto) {
    if (!dto.newPasswordsMatch()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas nuevas no coinciden.");
    }
    String email = auth.getName();
    User u = users.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    if (!encoder.matches(dto.getCurrentPassword(), u.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña actual incorrecta.");
    }

    u.setPasswordHash(encoder.encode(dto.getNewPassword()));
    users.save(u);
  }

  // Helper opcional: crear admin extra (protegido por ROLE_ADMIN desde el
  // controller)
  @Transactional
  public User createAdmin(String email, String rawPassword, String nombre) {
    if (users.existsByEmail(email)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado.");
    }
    User u = new User();
    u.setEmail(email.trim().toLowerCase());
    u.setNombre(nombre);
    u.setEnabled(true);
    u.setPasswordHash(encoder.encode(rawPassword));
    users.save(u);

    Role rAdmin = roles.findByName("ROLE_ADMIN")
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falta ROLE_ADMIN"));
    u.getRoles().add(rAdmin);

    return u;
  }
}
